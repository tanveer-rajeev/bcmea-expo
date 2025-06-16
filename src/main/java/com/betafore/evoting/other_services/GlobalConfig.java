package com.betafore.evoting.other_services;

import com.betafore.evoting.security_config.CustomCipherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    @Bean
    public CustomCipherService customCipherService(){
        return new CustomCipherService();
    }
}
