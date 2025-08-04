package com.betafore.evoting.ReportManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    void deleteAllByExpoId(Long expoId);
}
