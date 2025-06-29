package com.betafore.evoting.OtpManagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity,Long> {

    Optional<OtpEntity> findByPhoneNumber(String phoneNumber);
    Optional<OtpEntity> findByEmail(String email);
}
