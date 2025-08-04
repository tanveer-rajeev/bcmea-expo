package com.betafore.evoting.SmsService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsSettingsRepository extends JpaRepository<SmsSettings,Integer> {
    Optional<SmsSettings> findByExpoId(Long id);
    List<SmsSettings> findAllByExpoId(Long id);
}
