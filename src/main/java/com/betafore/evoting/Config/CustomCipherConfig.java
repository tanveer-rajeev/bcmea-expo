package com.betafore.evoting.Config;

import com.betafore.evoting.security_config.CustomCipherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomCipherConfig {

    @Bean
    public CustomCipherService customCipherService(){
        return new CustomCipherService();
    }
}
