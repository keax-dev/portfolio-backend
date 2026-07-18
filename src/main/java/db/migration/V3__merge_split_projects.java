package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class V3__merge_split_projects extends BaseJavaMigration {

    private static final Pattern LAYER_PREFIX = Pattern.compile("^(FRONTEND|BACKEND)\\s*-\\s*", Pattern.CASE_INSENSITIVE);

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        Map<String, List<ProjectRow>> groups = loadSplitProjectGroups(connection);

        for (List<ProjectRow> group : groups.values()) {
            boolean hasFrontend = group.stream().anyMatch(ProjectRow::frontend);
            boolean hasBackend = group.stream().anyMatch(ProjectRow::backend);
            if (!hasFrontend || !hasBackend) {
                continue;
            }

            ProjectRow target = group.stream()
                    .filter(ProjectRow::frontend)
                    .min(Comparator.comparingLong(ProjectRow::id))
                    .orElseThrow();

            normalizeTarget(connection, target);
            for (ProjectRow source : group) {
                if (source.id() == target.id()) {
                    continue;
                }
                copyTechnologies(connection, source.id(), target.id());
                copyLinks(connection, source.id(), target.id());
                clearProjectStructure(connection, source.id());
                softDeleteProject(connection, source.id());
            }
        }

        normalizeAllActiveTitles(connection);
        resequenceActiveProjects(connection);
    }

    private void normalizeAllActiveTitles(Connection connection) throws SQLException {
        List<ProjectRow> activeProjects = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("""
                select project_id, project_title, project_title_es, project_position
                from project
                where project_deleted = false
                order by project_id
                """); ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                activeProjects.add(new ProjectRow(
                        rows.getLong("project_id"),
                        rows.getString("project_title"),
                        rows.getString("project_title_es"),
                        rows.getInt("project_position")
                ));
            }
        }

        for (ProjectRow project : activeProjects) {
            normalizeTarget(connection, project);
        }
    }

    private Map<String, List<ProjectRow>> loadSplitProjectGroups(Connection connection) throws SQLException {
        Map<String, List<ProjectRow>> groups = new LinkedHashMap<>();
        try (PreparedStatement statement = connection.prepareStatement("""
                select project_id, project_title, project_title_es, project_position
                from project
                where project_deleted = false
                order by project_position, project_id
                """); ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                ProjectRow row = new ProjectRow(
                        rows.getLong("project_id"),
                        rows.getString("project_title"),
                        rows.getString("project_title_es"),
                        rows.getInt("project_position")
                );
                if (row.frontend() || row.backend()) {
                    groups.computeIfAbsent(normalizeKey(row.title()), ignored -> new ArrayList<>()).add(row);
                }
            }
        }
        return groups;
    }

    private void normalizeTarget(Connection connection, ProjectRow target) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("""
                update project
                set project_title = ?, project_title_es = ?
                where project_id = ?
                """)) {
            statement.setString(1, stripPrefix(target.title()));
            statement.setString(2, stripPrefix(target.titleEs()));
            statement.setLong(3, target.id());
            statement.executeUpdate();
        }
    }

    private void copyTechnologies(Connection connection, long sourceId, long targetId) throws SQLException {
        Set<Long> existingTechnologyIds = new LinkedHashSet<>();
        int nextPosition = 1;
        try (PreparedStatement statement = connection.prepareStatement("""
                select technology_id, project_technology_position
                from project_technology
                where project_id = ?
                order by project_technology_position
                """)) {
            statement.setLong(1, targetId);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    existingTechnologyIds.add(rows.getLong("technology_id"));
                    nextPosition = Math.max(nextPosition, rows.getInt("project_technology_position") + 1);
                }
            }
        }

        try (PreparedStatement source = connection.prepareStatement("""
                select technology_id
                from project_technology
                where project_id = ?
                order by project_technology_position
                """); PreparedStatement insert = connection.prepareStatement("""
                insert into project_technology (project_id, technology_id, project_technology_position)
                values (?, ?, ?)
                """)) {
            source.setLong(1, sourceId);
            try (ResultSet rows = source.executeQuery()) {
                while (rows.next()) {
                    long technologyId = rows.getLong("technology_id");
                    if (!existingTechnologyIds.add(technologyId)) {
                        continue;
                    }
                    insert.setLong(1, targetId);
                    insert.setLong(2, technologyId);
                    insert.setInt(3, nextPosition++);
                    insert.executeUpdate();
                }
            }
        }
    }

    private void copyLinks(Connection connection, long sourceId, long targetId) throws SQLException {
        Set<String> existingTypes = new LinkedHashSet<>();
        int nextPosition = 1;
        try (PreparedStatement statement = connection.prepareStatement("""
                select project_link_type, project_link_position
                from project_link
                where project_id = ?
                order by project_link_position
                """)) {
            statement.setLong(1, targetId);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    existingTypes.add(rows.getString("project_link_type"));
                    nextPosition = Math.max(nextPosition, rows.getInt("project_link_position") + 1);
                }
            }
        }

        try (PreparedStatement source = connection.prepareStatement("""
                select project_link_type, project_link_url
                from project_link
                where project_id = ?
                order by project_link_position
                """); PreparedStatement insert = connection.prepareStatement("""
                insert into project_link (project_id, project_link_type, project_link_url, project_link_position)
                values (?, ?, ?, ?)
                """)) {
            source.setLong(1, sourceId);
            try (ResultSet rows = source.executeQuery()) {
                while (rows.next()) {
                    String type = rows.getString("project_link_type");
                    if (!existingTypes.add(type)) {
                        continue;
                    }
                    insert.setLong(1, targetId);
                    insert.setString(2, type);
                    insert.setString(3, rows.getString("project_link_url"));
                    insert.setInt(4, nextPosition++);
                    insert.executeUpdate();
                }
            }
        }
    }

    private void clearProjectStructure(Connection connection, long projectId) throws SQLException {
        executeByProjectId(connection, "delete from project_link where project_id = ?", projectId);
        executeByProjectId(connection, "delete from project_technology where project_id = ?", projectId);
    }

    private void softDeleteProject(Connection connection, long projectId) throws SQLException {
        executeByProjectId(connection, "update project set project_deleted = true where project_id = ?", projectId);
    }

    private void resequenceActiveProjects(Connection connection) throws SQLException {
        List<Long> projectIds = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("""
                select project_id
                from project
                where project_deleted = false
                order by project_position, project_id
                """); ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                projectIds.add(rows.getLong("project_id"));
            }
        }

        try (PreparedStatement update = connection.prepareStatement("""
                update project set project_position = ? where project_id = ?
                """)) {
            for (int index = 0; index < projectIds.size(); index++) {
                update.setInt(1, index + 1);
                update.setLong(2, projectIds.get(index));
                update.addBatch();
            }
            update.executeBatch();
        }
    }

    private void executeByProjectId(Connection connection, String sql, long projectId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, projectId);
            statement.executeUpdate();
        }
    }

    private String normalizeKey(String title) {
        return stripPrefix(title).trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private String stripPrefix(String value) {
        if (value == null) {
            return null;
        }
        return LAYER_PREFIX.matcher(value).replaceFirst("").trim();
    }

    private record ProjectRow(long id, String title, String titleEs, int position) {
        boolean frontend() {
            return title != null && title.toUpperCase(Locale.ROOT).startsWith("FRONTEND");
        }

        boolean backend() {
            return title != null && title.toUpperCase(Locale.ROOT).startsWith("BACKEND");
        }
    }
}
