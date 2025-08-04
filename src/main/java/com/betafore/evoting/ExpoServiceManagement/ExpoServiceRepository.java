package com.betafore.evoting.ExpoServiceManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpoServiceRepository extends JpaRepository<ExpoService,Long> {

    Optional<ExpoService> findByName(String name);

    @Query("""
              select e from ExpoService e where e.name =?1 and e.status = 'enable'
        """)
    Optional<ExpoService> findEnableServiceByName(String name);

    @Query("""
              select e from ExpoService e where e.id =?1 and e.status = 'enable'
        """)
    Optional<ExpoService> findEnableServiceById(Long id);

}
