package com.keax.mapping;

import com.keax.auth.domain.model.Auth;
import com.keax.auth.infrastructure.in.web.mapper.AuthWebMapper;
import com.keax.education.domain.model.Education;
import com.keax.education.infrastructure.in.web.mapper.EducationWebMapper;
import com.keax.education.infrastructure.out.persistence.mapper.EducationPersistenceMapper;
import com.keax.email.domain.model.Contact;
import com.keax.email.infrastructure.in.web.mapper.ContactWebMapper;
import com.keax.institution.domain.model.Institution;
import com.keax.institution.infrastructure.in.web.mapper.InstitutionWebMapper;
import com.keax.institution.infrastructure.out.persistence.mapper.InstitutionPersistenceMapper;
import com.keax.profile.domain.model.Profile;
import com.keax.profile.infrastructure.in.web.mapper.ProfileWebMapper;
import com.keax.profile.infrastructure.out.persistence.mapper.ProfilePersistenceMapper;
import com.keax.project.domain.model.Project;
import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.infrastructure.in.web.mapper.SkillWebMapper;
import com.keax.skill.infrastructure.out.persistence.mapper.SkillPersistenceMapper;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.socialnetwork.infrastructure.in.web.mapper.SocialNetworkWebMapper;
import com.keax.socialnetwork.infrastructure.out.persistence.mapper.SocialNetworkPersistenceMapper;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.infrastructure.in.web.mapper.TechnologyWebMapper;
import com.keax.technology.infrastructure.out.persistence.mapper.TechnologyPersistenceMapper;
import com.keax.uploadimage.infrastructure.in.web.mapper.ImageFileWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica los contratos de mapeo web y persistencia para impedir pérdida,
 * intercambio accidental o exposición de campos sensibles entre capas.
 */
class MapperContractsTest {

    @Test
    void authMapperNeverReturnsPassword() {
        // Arrange: el dominio contiene credenciales y token.
        Auth auth = new Auth("admin", "secret", "jwt");

        // Act: se transforma a respuesta web y nuevamente a dominio.
        var dto = AuthWebMapper.fromDomain(auth);
        Auth roundTrip = AuthWebMapper.toDomain(dto);

        // Assert: username/token se conservan y password nunca sale en la respuesta.
        assertEquals("admin", roundTrip.getUsername());
        assertEquals("jwt", roundTrip.getToken());
        assertNull(dto.getPassword());
    }

    @Test
    void simpleWebMappersPreserveBusinessFields() {
        // Arrange: se preparan modelos de módulos sin relaciones complejas.
        Profile profile = new Profile(1L, "KEAX", "JIMENEZ", "DEV", "DEV", "cv", "picture");
        Institution institution = new Institution(2L, "UNI", "UNI", "url", false);
        Skill skill = new Skill(3L, "JAVA", "picture", 1, false);
        SocialNetwork social = new SocialNetwork(
                4L, "GITHUB", "icon", "#fff", 2, "https://github.com", false
        );
        Contact contact = new Contact("Keax", "keax@example.com", "Hello");

        // Act: cada modelo cruza dominio → DTO → dominio.
        Profile profileResult = ProfileWebMapper.toDomain(ProfileWebMapper.fromDomain(profile));
        Institution institutionResult = InstitutionWebMapper.toDomain(
                InstitutionWebMapper.fromDomain(institution)
        );
        Skill skillResult = SkillWebMapper.toDomain(SkillWebMapper.fromDomain(skill));
        SocialNetwork socialResult = SocialNetworkWebMapper.toDomain(
                SocialNetworkWebMapper.fromDomain(social)
        );
        Contact contactResult = ContactWebMapper.toDomain(ContactWebMapper.fromDomain(contact));

        // Assert: se verifican campos distintivos para detectar cruces de posición.
        assertEquals("picture", profileResult.getProfilePicture());
        assertEquals(2L, institutionResult.getInstitutionId());
        assertEquals(1, skillResult.getSkillPosition());
        assertEquals("https://github.com", socialResult.getSocialNetworkUrl());
        assertEquals("keax@example.com", contactResult.getEmail());
    }

    @Test
    void relationalWebMappersPreserveIdentifiersAndNestedProjects() {
        // Arrange: educación y proyecto contienen ids de sus módulos relacionados.
        Education education = education();
        Project project = project();
        Technology technology = new Technology(10L, "JAVA", 1, false, List.of(project));

        // Act: se ejecutan los mappers que incluyen relaciones.
        Education educationResult = EducationWebMapper.toDomain(
                EducationWebMapper.fromDomain(education)
        );
        Project projectResult = ProjectWebMapper.toDomain(ProjectWebMapper.fromDomain(project));
        Technology technologyResult = TechnologyWebMapper.toDomain(
                TechnologyWebMapper.fromDomain(technology)
        );

        // Assert: ids y colección anidada se conservan.
        assertEquals(20L, educationResult.getInstitutionId());
        assertEquals(10L, projectResult.getTechnologyId());
        assertEquals(1, technologyResult.getProjectList().size());
        assertEquals(30L, technologyResult.getProjectList().getFirst().getProjectId());
    }

    @Test
    void technologyWebMapperTreatsNullProjectListAsEmpty() {
        // Arrange: un DTO administrativo llega sin la propiedad de proyectos.
        var dto = TechnologyWebMapper.fromDomain(
                new Technology(10L, "JAVA", 1, false, List.of())
        );
        dto.setProjectList(null);

        // Act: se convierte al modelo de dominio.
        Technology result = TechnologyWebMapper.toDomain(dto);

        // Assert: el dominio recibe una colección utilizable, nunca null.
        assertEquals(0, result.getProjectList().size());
    }

    @Test
    void imageFileMapperReadsMultipartContent() {
        // Arrange: se construye un multipart real en memoria.
        byte[] bytes = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
        MockMultipartFile multipart = new MockMultipartFile(
                "image", "photo.jpg", "image/jpeg", bytes
        );

        // Act: se transforma al modelo independiente de Spring MVC.
        var result = ImageFileWebMapper.toDomain(multipart);

        // Assert: nombre, MIME y contenido quedan intactos.
        assertEquals("photo.jpg", result.getOriginalName());
        assertEquals("image/jpeg", result.getContentType());
        assertArrayEquals(bytes, result.getBytes());
        assertNull(ImageFileWebMapper.toDomain(null));
    }

    @Test
    void imageFileMapperTranslatesReadFailure() throws Exception {
        // Arrange: el multipart falla al leer su contenido.
        MultipartFile multipart = mock(MultipartFile.class);
        when(multipart.getOriginalFilename()).thenReturn("broken.jpg");
        when(multipart.getContentType()).thenReturn("image/jpeg");
        when(multipart.getBytes()).thenThrow(new IOException("read failed"));

        // Act y Assert: el detalle técnico no escapa de infraestructura.
        assertThrows(ExceptionMessage.class, () -> ImageFileWebMapper.toDomain(multipart));
    }

    @Test
    void simplePersistenceMappersRoundTripDomainFields() {
        // Arrange: se preparan modelos persistibles independientes.
        Profile profile = new Profile(1L, "KEAX", "JIMENEZ", "DEV", "DEV", "cv", "picture");
        Institution institution = new Institution(2L, "UNI", "UNI", "url", false);
        Skill skill = new Skill(3L, "JAVA", "picture", 1, false);
        SocialNetwork social = new SocialNetwork(
                4L, "GITHUB", "icon", "#fff", 2, "https://github.com", false
        );

        // Act: cada modelo cruza dominio → entidad JPA → dominio.
        Profile profileResult = ProfilePersistenceMapper.toDomain(
                ProfilePersistenceMapper.toEntity(profile)
        );
        Institution institutionResult = InstitutionPersistenceMapper.toDomain(
                InstitutionPersistenceMapper.toEntity(institution)
        );
        Skill skillResult = SkillPersistenceMapper.toDomain(
                SkillPersistenceMapper.toEntity(skill)
        );
        SocialNetwork socialResult = SocialNetworkPersistenceMapper.toDomain(
                SocialNetworkPersistenceMapper.toEntity(social)
        );

        // Assert: se conservan campos representativos y estado lógico.
        assertEquals("picture", profileResult.getProfilePicture());
        assertEquals("url", institutionResult.getInstitutionUrl());
        assertFalse(skillResult.getSkillDeleted());
        assertEquals("#fff", socialResult.getSocialNetworkColor());
    }

    @Test
    void relationalPersistenceMappersUseReferencesWithoutLoadingAggregates() {
        // Arrange: educación y proyecto solo necesitan ids de sus relaciones.
        Education education = education();
        Project project = project();

        // Act: se crean entidades con referencias livianas y se completan nombres
        // como lo haría Hibernate al materializar la relación.
        var educationEntity = EducationPersistenceMapper.toEntity(education);
        educationEntity.getInstitution().setInstitutionName("UNI");
        educationEntity.getInstitution().setInstitutionNameEs("UNI");
        var projectEntity = ProjectPersistenceMapper.toEntity(project);
        projectEntity.getTechnology().setTechnologyName("JAVA");
        Education educationResult = EducationPersistenceMapper.toDomain(educationEntity);
        Project projectResult = ProjectPersistenceMapper.toDomain(projectEntity);

        // Assert: ids relacionales y campos propios se conservan.
        assertEquals(20L, educationResult.getInstitutionId());
        assertEquals("DEGREE", educationResult.getEducationTitle());
        assertEquals(10L, projectResult.getTechnologyId());
        assertEquals("PROJECT", projectResult.getProjectTitle());
    }

    @Test
    void technologyPersistenceMapperKeepsProjectsOutOfWriteModel() {
        // Arrange: el dominio contiene proyectos, pero la relación la gobierna Project.
        Technology technology = new Technology(10L, "JAVA", 1, false, List.of(project()));

        // Act: se transforma al modelo de escritura y luego al dominio simple.
        var entity = TechnologyPersistenceMapper.toEntity(technology);
        Technology result = TechnologyPersistenceMapper.toDomain(entity);

        // Assert: no se propagan hijos accidentalmente durante un update de tecnología.
        assertEquals(0, entity.getProjectEntityList().size());
        assertEquals(0, result.getProjectList().size());
        assertEquals(10L, TechnologyPersistenceMapper.toReference(10L).getTechnologyId());
        assertEquals(20L, InstitutionPersistenceMapper.toReference(20L).getInstitutionId());
    }

    private Education education() {
        // Construye educación completa para mapeos web y JPA.
        return new Education(
                40L, "DEGREE", "TÍTULO", "UNIVERSITY", "2020", "2020",
                "2024", "2024", 1, false, 20L, "UNI", "UNI", "url"
        );
    }

    private Project project() {
        // Construye proyecto completo para mapeos web y JPA.
        return new Project(
                30L, "PROJECT", "PROYECTO", "Description", "Descripción",
                "picture", "deploy", "github", 1, 10L, "JAVA", false
        );
    }

}
