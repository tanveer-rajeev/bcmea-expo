package com.betafore.evoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EvotingApplication {


    public static void main(String[] args) {
        SpringApplication.run(EvotingApplication.class, args);
    }

//    @Bean
//    @Transactional
//    public CommandLineRunner commandLineRunner(OrganizerService service) {
//        fileStorageServiceImp.initStorage();
//        return args -> {
//            OrganizerDto superAdmin = OrganizerDto.builder()
//                .phoneNumber(phoneNumber)
//                .email(email)
//                .password(password)
//                .enabled(enabled)
//                .build();
//            service.createSuperAdmin(superAdmin, superAdminRole);
//        };
//    }

}
