package com.betafore.evoting.GiftManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftRepository extends JpaRepository<Gift,Long> {

    @Query("""
          select g from Gift g where g.expoId =?1
        """)
    List<Gift> findByExpoId(Long expoId);

    @Query("""
        select g from Gift  g where g.id =?1
        """)
    Optional<Gift> findGiftById(Long id);

    @Query("""
        select g from Gift  g where g.name =?1 and g.expoId =?2
        """)
    Optional<Gift> findGiftByNameAndExpoId(String name,Long expoId);

    @Query("""
          select count (*) from Gift g where g.expoId =?1
        """)
    Integer totalGift(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
