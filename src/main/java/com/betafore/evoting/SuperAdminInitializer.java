package com.betafore.evoting;

import com.betafore.evoting.OrganizerManagement.OrganizerDto;
import com.betafore.evoting.OrganizerManagement.OrganizerService;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements CommandLineRunner {

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

    private final OrganizerService service;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Super admin initializing");
        fileStorageServiceImp.initStorage();
        OrganizerDto superAdmin = OrganizerDto.builder()
            .phoneNumber(phoneNumber)
            .email(email)
            .password(password)
            .enabled(enabled)
            .build();
        service.createSuperAdmin(superAdmin, superAdminRole);
    }
}
