package db.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class V10CourseBilingualCertificatesTest {

    @Test
    void preservesExistingCertificateImagesAndBackfillsTheEnglishName() throws Exception {
        String url = "jdbc:h2:mem:course_bilingual_certificates;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        Flyway.configure()
                .dataSource(url, "sa", "")
                .target(MigrationVersion.fromVersion("9"))
                .load()
                .migrate();

        try (var connection = DriverManager.getConnection(url, "sa", "");
             var statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into institution (
                        institution_name,
                        institution_name_es,
                        institution_deleted
                    ) values ('UDEMY', 'UDEMY', false)
                    """);
            statement.executeUpdate("""
                    insert into course (
                        course_name,
                        course_certificate_url,
                        institution_id
                    ) values (
                        'SPRING BOOT',
                        'https://cdn.test/certificate.png',
                        1
                    )
                    """);
        }

        Flyway.configure()
                .dataSource(url, "sa", "")
                .load()
                .migrate();

        try (var connection = DriverManager.getConnection(url, "sa", "");
             var statement = connection.createStatement();
             var result = statement.executeQuery("""
                     select course_name_en, course_certificate_img, course_certificate_url
                     from course
                     """)) {
            result.next();
            assertEquals("SPRING BOOT", result.getString("course_name_en"));
            assertEquals(
                    "https://cdn.test/certificate.png",
                    result.getString("course_certificate_img")
            );
            assertNull(result.getString("course_certificate_url"));
        }
    }
}
