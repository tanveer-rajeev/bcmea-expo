package com.betafore.evoting.OtpManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity,Long> {

    Optional<OtpEntity> findByPhoneNumber(String phoneNumber);
    Optional<OtpEntity> findByEmail(String email);
}
