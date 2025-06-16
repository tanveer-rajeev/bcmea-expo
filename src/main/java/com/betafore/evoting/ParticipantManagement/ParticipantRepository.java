package com.betafore.evoting.ParticipantManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("""
          select p from Participant p where p.expoId =?1
        """)
    List<Participant> findAllByExpoId(Long expoId);

    @Query("""
        select p from Participant p where p.id =?1
        """)
    Optional<Participant> findGiftById(Long id);

    Optional<Participant> findByExpoIdAndId(Long expoId, Long id);

    void deleteAllByExpoId(Long expoId);
}
