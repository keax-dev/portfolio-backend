package com.keax.infrastructure.config;

import com.keax.application.services.Implementation.EducationServiceImpl;
import com.keax.application.services.Implementation.InstitutionServiceImpl;
import com.keax.application.usecases.Education.CreateEducationUseCaseImpl;
import com.keax.application.usecases.Education.DeleteEducationUseCaseImpl;
import com.keax.application.usecases.Education.RetrieveEducationUseCaseImpl;
import com.keax.application.usecases.Education.UpdateEducationUseCaseImpl;
import com.keax.application.usecases.Institution.CreateInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.DeleteInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.RetrieveInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.UpdateInstitutionUseCaseImpl;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.infrastructure.adapters.JpaEducationRepositoryAdapter;
import com.keax.infrastructure.adapters.JpaInstitutionRepositoryAdapter;
import com.keax.infrastructure.repositories.JpaEducationRepository;
import com.keax.infrastructure.repositories.JpaInstitutionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    //Institution Bean
    @Bean
    public InstitutionServiceImpl institutionService(InstitutionRepositoryPort institutionRepositoryPort){
        return new InstitutionServiceImpl(
                new CreateInstitutionUseCaseImpl(institutionRepositoryPort),
                new UpdateInstitutionUseCaseImpl(institutionRepositoryPort),
                new RetrieveInstitutionUseCaseImpl(institutionRepositoryPort),
                new DeleteInstitutionUseCaseImpl(institutionRepositoryPort)
        );
    }

    @Bean
    public InstitutionRepositoryPort institutionRepositoryPort(JpaInstitutionRepositoryAdapter jpaInstitutionRepositoryAdapter){
        return  jpaInstitutionRepositoryAdapter;
    }

    @Bean
    public JpaInstitutionRepositoryAdapter jpaInstitutionRepositoryAdapter(JpaInstitutionRepository repository) {
        return new JpaInstitutionRepositoryAdapter(repository);
    }

    //Education Bean
    @Bean
    public EducationServiceImpl educationService(EducationRepositoryPort educationRepositoryPort, InstitutionRepositoryPort institutionRepositoryPort){
        return  new EducationServiceImpl(
                new CreateEducationUseCaseImpl(educationRepositoryPort, institutionRepositoryPort),
                new UpdateEducationUseCaseImpl(educationRepositoryPort, institutionRepositoryPort),
                new RetrieveEducationUseCaseImpl(educationRepositoryPort),
                new DeleteEducationUseCaseImpl(educationRepositoryPort)
        );
    }

    @Bean
    public EducationRepositoryPort educationRepositoryPort(JpaEducationRepositoryAdapter jpaEducationRepositoryAdapter){
        return jpaEducationRepositoryAdapter;
    }

    @Bean
    public JpaEducationRepositoryAdapter jpaEducationRepositoryAdapter(JpaEducationRepository repository){
        return  new JpaEducationRepositoryAdapter(repository);
    }

}
