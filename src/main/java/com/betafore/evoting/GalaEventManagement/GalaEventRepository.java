package com.betafore.evoting.GalaEventManagement;

import com.betafore.evoting.PrizeMenagement.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalaEventRepository extends JpaRepository<GalaEvent,Long> {

    @Query("""
        select s from GalaEvent  s where s.id =?1
        """)
    Optional<GalaEvent> findGalaEventById(Long id);

    @Query("""
        select s from GalaEvent  s where s.name =?1 and s.expoId =?2
        """)
    Optional<GalaEvent> findGalaEventByNameAndExpoId(String name,Long expoId);

    @Query("""
          select v from GalaEvent v where v.expoId =?1
        """)
    List<GalaEvent> findAllByExpoId(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
