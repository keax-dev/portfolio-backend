package com.keax.portfolio.infrastructure.in.web.controller;

import lombok.RequiredArgsConstructor;

import com.keax.socialnetwork.infrastructure.in.web.mapper.SocialNetworkWebMapper;
import com.keax.technology.infrastructure.in.web.mapper.TechnologyWebMapper;
import com.keax.education.infrastructure.in.web.mapper.EducationWebMapper;
import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.socialnetwork.infrastructure.in.web.dto.SocialNetworkDTO;
import com.keax.profile.infrastructure.in.web.mapper.ProfileWebMapper;
import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import com.keax.email.infrastructure.in.web.mapper.ContactWebMapper;
import com.keax.technology.infrastructure.in.web.dto.TechnologyDTO;
import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.project.infrastructure.in.web.dto.ProjectDTO;
import com.keax.project.infrastructure.in.web.mapper.ProjectWebMapper;
import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.skill.infrastructure.in.web.mapper.SkillWebMapper;
import com.keax.education.infrastructure.in.web.dto.EducationDTO;
import com.keax.email.infrastructure.in.web.ratelimit.ContactRateLimiter;
import com.keax.profile.domain.ports.in.RetrieveProfileUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keax.profile.infrastructure.in.web.dto.ProfileDTO;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import com.keax.email.infrastructure.in.web.dto.ContactDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import com.keax.email.domain.ports.in.ContactEmailUseCase;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import com.keax.skill.infrastructure.in.web.dto.SkillDTO;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final RetrieveProfileUseCase retrieveProfileUseCase;
    private final RetrieveEducationUseCase retrieveEducationUseCase;
    private final RetrieveSkillUseCase retrieveSkillUseCase;
    private final RetrieveTechnologyUseCase retrieveTechnologyUseCase;
    private final RetrieveProjectUseCase retrieveProjectUseCase;
    private final RetrieveSocialNetworkUseCase retrieveSocialNetworkUseCase;
    private final ContactEmailUseCase contactEmailUseCase;
    private final ContactRateLimiter contactRateLimiter;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<ProfileDTO>> getProfile() {
        ApiResponseDTO<ProfileDTO> response = new ApiResponseDTO<>(
                true,
                "Profile information found successfully",
                ProfileWebMapper.fromDomain(
                        retrieveProfileUseCase.getProfile()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/education")
    public ResponseEntity<ApiResponseDTO<List<EducationDTO>>> getEducation() {
        ApiResponseDTO<List<EducationDTO>> response = new ApiResponseDTO<>(
                true,
                "Educational information found successfully",
                retrieveEducationUseCase.findByEducationDeleted(false).stream().map(EducationWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/skill")
    public ResponseEntity<ApiResponseDTO<List<SkillDTO>>> getSkill() {
        ApiResponseDTO<List<SkillDTO>> response = new ApiResponseDTO<>(
                true,
                "Skill information found successfully",
                retrieveSkillUseCase.findBySkillDeleted(false).stream().map(SkillWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/technology")
    public ResponseEntity<ApiResponseDTO<List<TechnologyDTO>>> getTechnology() {
        ApiResponseDTO<List<TechnologyDTO>> response = new ApiResponseDTO<>(
                true,
                "Technology information found successfully",
                retrieveTechnologyUseCase.findByTechnologyDeleted(false)
                        .stream().map(TechnologyWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/project")
    public ResponseEntity<ApiResponseDTO<List<ProjectDTO>>> getProject() {
        ApiResponseDTO<List<ProjectDTO>> response = new ApiResponseDTO<>(
                true,
                "Project information found successfully",
                retrieveProjectUseCase.findByProjectDeleted(false)
                        .stream().map(ProjectWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/socialNetwork")
    public ResponseEntity<ApiResponseDTO<List<SocialNetworkDTO>>> getSocialNetwork() {
        ApiResponseDTO<List<SocialNetworkDTO>> response = new ApiResponseDTO<>(
                true,
                "Social Network information found successfully",
                retrieveSocialNetworkUseCase.findBySocialNetworkDeleted(false).stream().map(SocialNetworkWebMapper::fromDomain).toList()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/contact")
    public ResponseEntity<ApiResponseDTO<ContactDTO>> sendContact(@Valid @RequestBody ContactDTO contact, HttpServletRequest request) {
        contactRateLimiter.assertAllowed(request.getRemoteAddr());

        ApiResponseDTO<ContactDTO> response = new ApiResponseDTO<>(
                true,
                "The email has been sent correctly",
                ContactWebMapper.fromDomain(
                        contactEmailUseCase.sendContactEmail(ContactWebMapper.toDomain(contact))
                )
        );

        return ResponseEntity.ok(response);
    }

}
