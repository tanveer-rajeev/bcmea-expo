package com.betafore.evoting.SmsConfig;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SmsSettingsRepository extends JpaRepository<SmsSettings,Integer> {

    Optional<SmsSettings> findByExpoId(Long id);
    List<SmsSettings> findAllByExpoId(Long id);
}
