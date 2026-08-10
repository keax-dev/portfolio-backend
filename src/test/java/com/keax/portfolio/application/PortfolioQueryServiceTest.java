package com.keax.portfolio.application;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.RetrieveCourseUseCase;
import com.keax.education.domain.model.Education;
import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.in.RetrieveExperienceUseCase;
import com.keax.profile.domain.model.Profile;
import com.keax.profile.domain.ports.in.RetrieveProfileUseCase;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PortfolioQueryServiceTest {

    @Test
    void delegatesEveryPublicSectionToItsApplicationPort() {
        RetrieveProfileUseCase profilePort = mock(RetrieveProfileUseCase.class);
        RetrieveEducationUseCase educationPort = mock(RetrieveEducationUseCase.class);
        RetrieveExperienceUseCase experiencePort = mock(RetrieveExperienceUseCase.class);
        RetrieveCourseUseCase coursePort = mock(RetrieveCourseUseCase.class);
        RetrieveSkillUseCase skillPort = mock(RetrieveSkillUseCase.class);
        RetrieveTechnologyUseCase technologyPort = mock(RetrieveTechnologyUseCase.class);
        RetrieveProjectUseCase projectPort = mock(RetrieveProjectUseCase.class);
        RetrieveSocialNetworkUseCase socialNetworkPort = mock(RetrieveSocialNetworkUseCase.class);

        Profile profile = new Profile();
        List<Education> education = List.of(new Education());
        List<Experience> experiences = List.of(new Experience());
        List<Course> courses = List.of(new Course());
        List<Skill> skills = List.of(new Skill());
        List<Technology> technologies = List.of(new Technology());
        List<Project> projects = List.of(new Project());
        List<SocialNetwork> socialNetworks = List.of(new SocialNetwork());

        when(profilePort.getProfile()).thenReturn(profile);
        when(educationPort.getVisibleEducation()).thenReturn(education);
        when(experiencePort.getVisibleExperiences()).thenReturn(experiences);
        when(coursePort.getVisibleCourses()).thenReturn(courses);
        when(skillPort.getVisibleSkills()).thenReturn(skills);
        when(technologyPort.getListTechnology()).thenReturn(technologies);
        when(projectPort.getPublishedProjects()).thenReturn(projects);
        when(socialNetworkPort.getListSocialNetwork()).thenReturn(socialNetworks);

        PortfolioQueryService service = new PortfolioQueryService(
                profilePort,
                educationPort,
                experiencePort,
                coursePort,
                skillPort,
                technologyPort,
                projectPort,
                socialNetworkPort
        );

        assertSame(profile, service.getProfile());
        assertSame(education, service.getEducation());
        assertSame(experiences, service.getExperiences());
        assertSame(courses, service.getCourses());
        assertSame(skills, service.getSkills());
        assertSame(technologies, service.getTechnologies());
        assertSame(projects, service.getPublishedProjects());
        assertSame(socialNetworks, service.getSocialNetworks());
    }
}
