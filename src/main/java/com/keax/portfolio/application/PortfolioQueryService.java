package com.keax.portfolio.application;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.RetrieveCourseUseCase;
import com.keax.education.domain.model.Education;
import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.portfolio.domain.ports.in.PortfolioQueryUseCase;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioQueryService implements PortfolioQueryUseCase {

    private final RetrieveProfileUseCase profile;
    private final RetrieveEducationUseCase education;
    private final RetrieveCourseUseCase courses;
    private final RetrieveSkillUseCase skills;
    private final RetrieveTechnologyUseCase technologies;
    private final RetrieveProjectUseCase projects;
    private final RetrieveSocialNetworkUseCase socialNetworks;

    @Override
    public Profile getProfile() {
        return profile.getProfile();
    }

    @Override
    public List<Education> getEducation() {
        return education.getListEducation();
    }

    @Override
    public List<Course> getCourses() {
        return courses.getListCourse();
    }

    @Override
    public List<Skill> getSkills() {
        return skills.getListSkill();
    }

    @Override
    public List<Technology> getTechnologies() {
        return technologies.getListTechnology();
    }

    @Override
    public List<Project> getPublishedProjects() {
        return projects.getPublishedProjects();
    }

    @Override
    public List<SocialNetwork> getSocialNetworks() {
        return socialNetworks.getListSocialNetwork();
    }
}
