package com.example.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
@Configuration
public class Auditingconfig {

    @Bean

    public AuditorAware<String> auditionAware (){
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor(){
                return Optional.empty();
            }
        };
    }


}
