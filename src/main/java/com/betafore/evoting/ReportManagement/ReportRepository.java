package com.betafore.evoting.ReportManagement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {

    void deleteAllByExpoId(Long expoId);
}
