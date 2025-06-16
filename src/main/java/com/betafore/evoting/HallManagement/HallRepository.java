package com.betafore.evoting.HallManagement;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    Optional<Hall> findBySlug(String slug);

    Optional<Hall> findByNameAndExpoId(String name,Long expoId);

    Optional<Hall> findByIdAndExpoId(Long hallId,Long expoId);

    @Transactional
    void deleteBySlug(String slug);

    @Query("""
          select count (*) from Hall h where h.expoId =?1  
        """)
    Integer totalHall(Long expoId);

    List<Hall> findAllByExpoId(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
