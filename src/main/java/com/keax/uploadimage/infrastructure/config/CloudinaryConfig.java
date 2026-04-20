package com.keax.uploadimage.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.*;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String secretKey;

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
