package com.keax.management;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.auth.infrastructure.out.security.JwtUtil;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import com.keax.profile.infrastructure.out.persistence.repository.JpaProfileRepository;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.skill.infrastructure.out.persistence.repository.JpaSkillRepository;
import com.keax.socialnetwork.infrastructure.out.persistence.repository.JpaSocialNetworkRepository;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Ejecuta flujos administrativos end-to-end dentro del backend: HTTP, JWT,
 * validación, casos de uso, mappers y JPA para todos los módulos CRUD.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagementApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JpaProfileRepository profileRepository;
    @Autowired
    private JpaInstitutionRepository institutionRepository;
    @Autowired
    private JpaEducationRepository educationRepository;
    @Autowired
    private JpaTechnologyRepository technologyRepository;
    @Autowired
    private JpaProjectRepository projectRepository;
    @Autowired
    private JpaSkillRepository skillRepository;
    @Autowired
    private JpaSocialNetworkRepository socialNetworkRepository;

    @Test
    void managesProfileLifecycle() throws Exception {
        // Arrange: se obtiene un JWT administrativo.
        String token = token();

        // Act y Assert: se crea, actualiza y consulta el perfil único.
        performPost("/api/profile", """
                {"name":"Keax","last_name":"Jimenez","title":"Developer",
                 "title_es":"Desarrollador","cv":"","image":null}
                """, token).andExpect(status().isOk());

        performPut("/api/profile", """
                {"name":"Keax","last_name":"Jimenez","title":"Senior Developer",
                 "title_es":"Desarrollador Senior","cv":"https://example.com/cv","image":null}
                """, token).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("SENIOR DEVELOPER"));

        mockMvc.perform(get("/api/profile").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cv").value("https://example.com/cv"));

        // Assert adicional: el flujo persistió un único perfil.
        org.junit.jupiter.api.Assertions.assertEquals(1, profileRepository.count());
    }

    @Test
    void protectsInstitutionFromActiveEducationThenAllowsLogicalDeletion() throws Exception {
        // Arrange: se crea una institución y se obtiene su id persistido.
        String token = token();
        performPost("/api/institution", """
                {"name":"University","name_es":"Universidad","url":"","deleted":false}
                """, token).andExpect(status().isOk());
        Long institutionId = institutionRepository.findAll().getFirst().getInstitutionId();

        // Act: se crea educación asociada.
        performPost("/api/education", """
                {"title":"Degree","title_es":"Titulo","place":"University",
                 "start":"2020","start_es":"2020","end":"2024","end_es":"2024",
                 "position":1,"deleted":false,"institution":%d}
                """.formatted(institutionId), token).andExpect(status().isOk());
        Long educationId = educationRepository.findAll().getFirst().getEducationId();

        // Assert: la institución no puede eliminarse mientras exista educación activa.
        mockMvc.perform(delete("/api/institution/{id}", institutionId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isConflict());

        // Act y Assert: tras borrar lógicamente educación, se permite borrar institución.
        mockMvc.perform(delete("/api/education/{id}", educationId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/institution/{id}", institutionId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());

        // Assert adicional: el endpoint no retorna cuerpo, pero el registro quedó eliminado lógicamente.
        assertTrue(institutionRepository.findById(institutionId).orElseThrow().getInstitutionDeleted());
    }

    @Test
    void protectsTechnologyFromActiveProjectThenAllowsLogicalDeletion() throws Exception {
        // Arrange: se crea tecnología y se recupera su id.
        String token = token();
        performPost("/api/technology", """
                {"name":"Java","position":1,"deleted":false,"projects":[]}
                """, token).andExpect(status().isOk());
        Long technologyId = technologyRepository.findAll().getFirst().getTechnologyId();

        // Act: se crea un proyecto asociado.
        performPost("/api/project", """
                {"title":"Portfolio","title_es":"Portafolio",
                 "description":"Backend project","description_es":"Proyecto backend",
                 "deploy":"","github":"","position":1,"technology":%d,"deleted":false}
                """.formatted(technologyId), token).andExpect(status().isOk());
        Long projectId = projectRepository.findAll().getFirst().getProjectId();

        // Assert: la tecnología referenciada no puede eliminarse.
        mockMvc.perform(delete("/api/technology/{id}", technologyId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isConflict());

        // Act y Assert: al borrar el proyecto se habilita el borrado lógico padre.
        mockMvc.perform(delete("/api/project/{id}", projectId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/technology/{id}", technologyId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());

        // Assert adicional: se verifica el efecto persistido del comando DELETE.
        assertTrue(technologyRepository.findById(technologyId).orElseThrow().getTechnologyDeleted());
    }

    @Test
    void managesSkillAndSocialNetworkCrud() throws Exception {
        // Arrange: se autentica la sesión administrativa.
        String token = token();

        // Act: se crean habilidad y red social.
        performPost("/api/skill", """
                {"name":"Java","position":1,"deleted":false}
                """, token).andExpect(status().isOk());
        performPost("/api/socialNetwork", """
                {"name":"Github","icon":"github","color":"#ffffff","position":1,
                 "url":"https://github.com/keax","deleted":false}
                """, token).andExpect(status().isOk());
        Long skillId = skillRepository.findAll().getFirst().getSkillId();
        Long socialId = socialNetworkRepository.findAll().getFirst().getSocialNetworkId();

        // Act: se actualizan ambos recursos.
        performPut("/api/skill/" + skillId, """
                {"name":"Spring","position":2,"deleted":false}
                """, token).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("SPRING"));
        performPut("/api/socialNetwork/" + socialId, """
                {"name":"LinkedIn","icon":"linkedin","color":"#0000ff","position":2,
                 "url":"https://linkedin.com/in/keax","deleted":false}
                """, token).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("LINKEDIN"));

        // Assert: los listados administrativos exponen los cambios.
        mockMvc.perform(get("/api/skill").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].position").value(2));
        mockMvc.perform(get("/api/socialNetwork").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].position").value(2));
    }

    @Test
    void rejectsInvalidManagementPayload() throws Exception {
        // Arrange: se autentica, pero se envía una habilidad estructuralmente inválida.
        String token = token();

        // Act y Assert: Bean Validation responde 400 y no persiste datos.
        performPost("/api/skill", """
                {"name":"","position":0}
                """, token)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.alert").value("Validation error"));
        org.junit.jupiter.api.Assertions.assertEquals(0, skillRepository.count());
    }

    private org.springframework.test.web.servlet.ResultActions performPost(
            String path,
            String json,
            String token
    ) throws Exception {
        // Ejecuta un POST JSON autenticado de forma uniforme.
        return mockMvc.perform(post(path)
                .header("Authorization", bearer(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private org.springframework.test.web.servlet.ResultActions performPut(
            String path,
            String json,
            String token
    ) throws Exception {
        // Ejecuta un PUT JSON autenticado de forma uniforme.
        return mockMvc.perform(put(path)
                .header("Authorization", bearer(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private String token() {
        // Persiste el subject que UserDetailsService validará durante el filtro JWT.
        String username = "management-" + System.nanoTime();
        userRepository.save(new UserEntity(
                null,
                username,
                passwordEncoder.encode("irrelevant-password")
        ));
        return jwtUtil.generateToken(username);
    }

    private String bearer(String token) {
        // Centraliza el formato de la cabecera Authorization.
        return "Bearer " + token;
    }

}
