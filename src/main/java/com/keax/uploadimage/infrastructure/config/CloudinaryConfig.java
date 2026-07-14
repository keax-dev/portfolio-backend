package com.keax.uploadimage.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.*;

@Configuration
public class CloudinaryConfig {

    private final String cloudName;
    private final String apiKey;
    private final String secretKey;

    public CloudinaryConfig(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String secretKey
    ) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Bean
    Cloudinary cloudinary(){
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", this.cloudName,
                        "api_key", this.apiKey,
                        "api_secret", this.secretKey,
                        "secure", true
                )
        );
    }

}
