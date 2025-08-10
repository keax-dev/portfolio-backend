package com.keax.infrastructure.config;

import com.keax.application.services.InstitutionService;
import com.keax.application.usecases.Institution.CreateInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.DeleteInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.RetrieveInstitutionUseCaseImpl;
import com.keax.application.usecases.Institution.UpdateInstitutionUseCaseImpl;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.infrastructure.adapters.JpaInstitutionRepositoryAdapter;
import com.keax.infrastructure.repositories.JpaInstitutionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public InstitutionService institutionService(InstitutionRepositoryPort institutionRepositoryPort){
        return new InstitutionService(
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

}
