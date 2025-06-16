package com.betafore.evoting.EmailConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailSettingsRepository extends JpaRepository<EmailSettings,Long> {
    @Query("""
        select e from EmailSettings  e where e.expoId = ?1
        """)
    Optional<EmailSettings> findByExpoIdAndActive(Long id);

    Optional<EmailSettings> findBySmtpUsername(String username);

}
