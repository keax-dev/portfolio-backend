package db.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class V11CoursePositionTest {

    @Test
    void assignsStablePositivePositionsToExistingCourses() throws Exception {
        String url = "jdbc:h2:mem:course_positions;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
        Flyway.configure()
                .dataSource(url, "sa", "")
                .target(MigrationVersion.fromVersion("10"))
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
                        course_name_en,
                        institution_id
                    ) values
                        ('CURSO DOS', 'COURSE TWO', 1),
                        ('CURSO UNO', 'COURSE ONE', 1)
                    """);
        }

        Flyway.configure()
                .dataSource(url, "sa", "")
                .load()
                .migrate();

        try (var connection = DriverManager.getConnection(url, "sa", "");
             var statement = connection.createStatement();
             var result = statement.executeQuery("""
                     select course_position
                     from course
                     order by course_id
                     """)) {
            result.next();
            assertEquals(1, result.getInt("course_position"));
            result.next();
            assertEquals(2, result.getInt("course_position"));
        }
    }
}
