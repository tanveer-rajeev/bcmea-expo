package com.betafore.evoting.PrizeMenagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrizeRepository extends JpaRepository<Prize,Long> {

    @Query("""
          select v from Prize v where v.expoId =?1
        """)
    List<Prize> findAllByExpoId(Long expoId);

    Optional<Prize> findByNameAndExpoId(String name,Long expoId);

    void deleteAllByExpoId(Long expoId);

}
