package com.betafore.evoting;

import com.betafore.evoting.OrganizerManagement.OrganizerDto;
import com.betafore.evoting.OrganizerManagement.OrganizerService;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@SpringBootApplication
public class EvotingApplication {

    @Resource
    private FileStorageServiceImp fileStorageServiceImp;


    @Value("${application.email}")
    private String email;

    @Value("${application.phoneNumber}")
    private String phoneNumber;

    @Value("${application.password}")
    private String password;

    @Value("${application.enabled}")
    private String enabled;

    @Value("${application.role}")
    private String superAdminRole;

    public static void main(String[] args) {
        SpringApplication.run(EvotingApplication.class, args);
    }


    @Bean
    @Transactional
    public CommandLineRunner commandLineRunner(OrganizerService service) {
        fileStorageServiceImp.initStorage();
        return args -> {
            OrganizerDto superAdmin = OrganizerDto.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .password(password)
                .enabled(enabled)
                .build();
            service.createSuperAdmin(superAdmin, superAdminRole);
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.zoho.com");
        mailSender.setPort(465);
        mailSender.setUsername("info@bcmea-vote.com");
        mailSender.setPassword("Bcmea1122@");
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.debug", "true");
        return mailSender;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedMethods("GET","POST","PUT","DELETE")
                    .allowedOrigins("*")
                    .allowedHeaders("*");
            }
        };
    }
}

    
