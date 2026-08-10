package com.keax.portfolio.domain.ports.in;

import com.keax.course.domain.model.Course;
import com.keax.education.domain.model.Education;
import com.keax.experience.domain.model.Experience;
import com.keax.profile.domain.model.Profile;
import com.keax.project.domain.model.Project;
import com.keax.skill.domain.model.Skill;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.technology.domain.model.Technology;

import java.util.List;

public interface PortfolioQueryUseCase {

    Profile getProfile();

    List<Education> getEducation();

    List<Experience> getExperiences();

    List<Course> getCourses();

    List<Skill> getSkills();

    List<Technology> getTechnologies();

    List<Project> getPublishedProjects();

    List<SocialNetwork> getSocialNetworks();
}
