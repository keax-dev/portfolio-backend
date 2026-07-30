package db.migration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class V12OptimisticLockingTest {

    @Test
    void addsInitializedVersionColumnsToMutableAggregates() throws Exception {
        String url = "jdbc:h2:mem:optimistic_locking;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        Flyway.configure()
                .dataSource(url, "sa", "")
                .load()
                .migrate();

        List<String> tables = List.of(
                "institution",
                "education",
                "skill",
                "technology",
                "social_network",
                "project",
                "course",
                "profile"
        );

        try (var connection = DriverManager.getConnection(url, "sa", "")) {
            for (String table : tables) {
                try (var statement = connection.prepareStatement("""
                        select count(*)
                        from information_schema.columns
                        where lower(table_name) = ?
                          and lower(column_name) = 'version'
                          and is_nullable = 'NO'
                        """)) {
                    statement.setString(1, table);
                    try (var result = statement.executeQuery()) {
                        result.next();
                        assertEquals(1, result.getInt(1), table);
                    }
                }
            }
        }
    }
}
