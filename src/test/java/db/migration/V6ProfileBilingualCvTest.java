package db.migration;

import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class V6ProfileBilingualCvTest {

    @Test
    void copiesTheExistingCvIntoTheSpanishCvDuringMigration() throws Exception {
        String url = "jdbc:h2:mem:profile_bilingual_cv;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        Flyway.configure()
                .dataSource(url, "sa", "")
                .target(MigrationVersion.fromVersion("5"))
                .load()
                .migrate();

        try (var connection = DriverManager.getConnection(url, "sa", "");
             var statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into profile (
                        profile_name,
                        profile_last_name,
                        profile_title,
                        profile_title_es,
                        profile_cv
                    ) values (
                        'KEAX',
                        'JIMENEZ',
                        'DEVELOPER',
                        'DESARROLLADOR',
                        'https://example.com/cv-existing'
                    )
                    """);
        }

        Flyway.configure()
                .dataSource(url, "sa", "")
                .load()
                .migrate();

        try (var connection = DriverManager.getConnection(url, "sa", "");
             var statement = connection.createStatement();
             var result = statement.executeQuery("select profile_cv, profile_cv_es from profile")) {
            result.next();
            assertEquals("https://example.com/cv-existing", result.getString("profile_cv"));
            assertEquals("https://example.com/cv-existing", result.getString("profile_cv_es"));
        }
    }
}
