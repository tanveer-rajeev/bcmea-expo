package com.betafore.evoting.StallManagement;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StallRepository extends JpaRepository<Stall, Long> {
    Optional<Stall> findBySlug(String slug);

    Optional<Stall> findByName(String slug);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Stall s where s.slug = ?1")
    void deleteBySlug(String slug);

    @Query("""
          select count (*) from Stall s where s.expoId =?1
        """)
    Integer totalStall(Long expoId);

    void deleteAllByExpoId(Long expoId);
    void deleteById(Long id);

    List<Stall> findAllByExpoId(Long expoId);

    Optional<Stall> findByNameAndExpoId(String name,Long expoId);
}
