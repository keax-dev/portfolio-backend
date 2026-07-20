package com.keax.uploadimage.application.services;

import com.keax.institution.domain.model.Institution;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.profile.domain.model.Profile;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageCleanupTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ImagePersistenceCoordinator {

    private final ProjectRepositoryPort projectRepositoryPort;
    private final InstitutionRepositoryPort institutionRepositoryPort;
    private final ProfileRepositoryPort profileRepositoryPort;
    private final SkillRepositoryPort skillRepositoryPort;
    private final ImageCleanupTaskPort cleanupTaskPort;

    @Transactional
    public Project updateProject(Project project, Collection<String> obsoleteUrls) {
        Project updated = projectRepositoryPort.updateProject(project);
        cleanupTaskPort.enqueueAll(obsoleteUrls);
        return updated;
    }

    @Transactional
    public Institution updateInstitution(Institution institution, Collection<String> obsoleteUrls) {
        Institution updated = institutionRepositoryPort.updateInstitution(institution);
        cleanupTaskPort.enqueueAll(obsoleteUrls);
        return updated;
    }

    @Transactional
    public Profile updateProfile(Profile profile, Collection<String> obsoleteUrls) {
        Profile updated = profileRepositoryPort.saveProfile(profile);
        cleanupTaskPort.enqueueAll(obsoleteUrls);
        return updated;
    }

    @Transactional
    public Skill updateSkill(Skill skill, Collection<String> obsoleteUrls) {
        Skill updated = skillRepositoryPort.updateSkill(skill);
        cleanupTaskPort.enqueueAll(obsoleteUrls);
        return updated;
    }
}
