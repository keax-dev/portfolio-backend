package com.keax.experience.domain.ports.out;

import com.keax.experience.domain.model.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepositoryPort {

    Experience save(Experience experience);

    Experience delete(Experience experience);

    List<Experience> findAll();

    List<Experience> findVisible();

    Optional<Experience> findById(Long experienceId);

    Optional<Experience> findByPosition(int position);
}
