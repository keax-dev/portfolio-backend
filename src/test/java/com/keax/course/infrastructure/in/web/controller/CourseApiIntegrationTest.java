package com.keax.course.infrastructure.in.web.controller;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.auth.infrastructure.out.security.JwtUtil;
import com.keax.course.infrastructure.out.persistence.repository.JpaCourseRepository;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CourseApiIntegrationTest {

    private final MockMvc mockMvc;
    private final JpaCourseRepository courseRepository;
    private final JpaInstitutionRepository institutionRepository;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JdbcTemplate jdbcTemplate;

    CourseApiIntegrationTest(
            MockMvc mockMvc,
            JpaCourseRepository courseRepository,
            JpaInstitutionRepository institutionRepository,
            JpaUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            JdbcTemplate jdbcTemplate
    ) {
        this.mockMvc = mockMvc;
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void managesAndPublishesCoursesWithLogicalDeletion() throws Exception {
        InstitutionEntity institution = institutionRepository.saveAndFlush(new InstitutionEntity(
                null,
                "UDEMY",
                "UDEMY",
                null,
                false
        ));
        String token = token();

        mockMvc.perform(post("/api/course")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Spring Boot desde cero",
                                  "name_en":"Spring Boot Masterclass",
                                  "certificate_url":"https://udemy.test/certificate/123",
                                  "position":1,
                                  "institution":%d
                                }
                                """.formatted(institution.getInstitutionId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("SPRING BOOT DESDE CERO"))
                .andExpect(jsonPath("$.data.name_en").value("SPRING BOOT MASTERCLASS"))
                .andExpect(jsonPath("$.data.certificate_img").doesNotExist())
                .andExpect(jsonPath("$.data.certificate_url")
                        .value("https://udemy.test/certificate/123"))
                .andExpect(jsonPath("$.data.position").value(1))
                .andExpect(jsonPath("$.data.institution_name").value("UDEMY"));

        Long courseId = courseRepository.findAll().getFirst().getCourseId();

        mockMvc.perform(put("/api/course/{id}", courseId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Spring Boot moderno",
                                  "name_en":"Modern Spring Boot",
                                  "certificate_url":"",
                                  "position":1,
                                  "institution":%d
                                }
                                """.formatted(institution.getInstitutionId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("SPRING BOOT MODERNO"))
                .andExpect(jsonPath("$.data.name_en").value("MODERN SPRING BOOT"))
                .andExpect(jsonPath("$.data.position").value(1))
                .andExpect(jsonPath("$.data.certificate_url").doesNotExist());

        mockMvc.perform(get("/api/course").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));

        mockMvc.perform(get("/api/portfolio/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("SPRING BOOT MODERNO"))
                .andExpect(jsonPath("$.data[0].name_en").value("MODERN SPRING BOOT"));

        mockMvc.perform(delete("/api/course/{id}", courseId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk());

        assertTrue(courseRepository.findById(courseId).isEmpty());
        Boolean deleted = jdbcTemplate.queryForObject(
                "select course_deleted from course where course_id = ?",
                Boolean.class,
                courseId
        );
        assertTrue(Boolean.TRUE.equals(deleted));

        mockMvc.perform(get("/api/portfolio/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    private String token() {
        String username = "course-management-" + System.nanoTime();
        userRepository.save(new UserEntity(
                null,
                username,
                passwordEncoder.encode("irrelevant-password")
        ));
        return jwtUtil.generateToken(username);
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
