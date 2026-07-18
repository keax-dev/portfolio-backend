package db.migration;

import org.flywaydb.core.api.migration.Context;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class V3MergeSplitProjectsTest {

    @Test
    void mergesFrontendAndBackendRecordsAndNormalizesStandaloneTitles() throws Exception {
        String database = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";
        try (Connection connection = DriverManager.getConnection(database)) {
            createSchema(connection);
            seedProjects(connection);
            Context context = mock(Context.class);
            when(context.getConnection()).thenReturn(connection);

            new V3__merge_split_projects().migrate(context);

            assertProject(connection, 1, "COURIER OPERATIONS PLATFORM", false, 1);
            assertProject(connection, 4, "BACKEND - COURIER OPERATIONS PLATFORM", true, 1);
            assertProject(connection, 11, "FLOWER CATALOGUE", false, 2);
            assertEquals(2, count(connection, "project_technology", "project_id = 1"));
            assertEquals(2, count(connection, "project_link", "project_id = 1"));
            assertEquals(0, count(connection, "project_technology", "project_id = 4"));
            assertEquals(0, count(connection, "project_link", "project_id = 4"));
        }
    }

    private void createSchema(Connection connection) throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table project (
                        project_id bigint primary key,
                        project_title varchar(255) not null,
                        project_title_es varchar(255),
                        project_position integer not null,
                        project_deleted boolean not null
                    )
                    """);
            statement.execute("""
                    create table project_technology (
                        project_id bigint not null,
                        technology_id bigint not null,
                        project_technology_position integer not null,
                        unique (project_id, technology_id),
                        unique (project_id, project_technology_position)
                    )
                    """);
            statement.execute("""
                    create table project_link (
                        project_id bigint not null,
                        project_link_type varchar(40) not null,
                        project_link_url varchar(2048) not null,
                        project_link_position integer not null,
                        unique (project_id, project_link_type),
                        unique (project_id, project_link_position)
                    )
                    """);
        }
    }

    private void seedProjects(Connection connection) throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into project values
                    (1, 'FRONTEND - COURIER OPERATIONS PLATFORM',
                        'FRONTEND - PLATAFORMA DE OPERACIONES COURIER', 1, false),
                    (4, 'BACKEND - COURIER OPERATIONS PLATFORM',
                        'BACKEND - PLATAFORMA DE OPERACIONES COURIER', 1, false),
                    (11, 'FRONTEND - FLOWER CATALOGUE',
                        'FRONTEND - CATÁLOGO DE FLORES', 6, false)
                    """);
            statement.executeUpdate("""
                    insert into project_technology values (1, 1, 1), (4, 3, 1), (11, 1, 1)
                    """);
            statement.executeUpdate("""
                    insert into project_link values
                    (1, 'DEPLOY', 'https://example.com', 1),
                    (4, 'GITHUB_BACKEND', 'https://github.com/example/api', 1)
                    """);
        }
    }

    private void assertProject(
            Connection connection,
            long id,
            String title,
            boolean deleted,
            int position
    ) throws Exception {
        try (Statement statement = connection.createStatement(); ResultSet row = statement.executeQuery(
                "select project_title, project_deleted, project_position from project where project_id = " + id
        )) {
            assertTrue(row.next());
            assertEquals(title, row.getString("project_title"));
            if (deleted) {
                assertTrue(row.getBoolean("project_deleted"));
            } else {
                assertFalse(row.getBoolean("project_deleted"));
            }
            assertEquals(position, row.getInt("project_position"));
        }
    }

    private int count(Connection connection, String table, String condition) throws Exception {
        try (Statement statement = connection.createStatement(); ResultSet row = statement.executeQuery(
                "select count(*) from " + table + " where " + condition
        )) {
            row.next();
            return row.getInt(1);
        }
    }
}
