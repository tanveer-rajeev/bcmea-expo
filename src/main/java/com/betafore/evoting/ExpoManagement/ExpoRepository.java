package com.betafore.evoting.ExpoManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpoRepository extends JpaRepository<Expo,Long> {
    Optional<Expo> findByExpoName(String expoName);
    @Query("""
        select e from Expo e where e.status = "enable" and e.id =?1
        """)
    Optional<Expo> findEnableExpoById(Long id);
}
