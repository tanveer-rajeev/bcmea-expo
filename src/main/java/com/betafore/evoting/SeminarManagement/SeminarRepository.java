package com.betafore.evoting.SeminarManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SeminarRepository extends JpaRepository<Seminar,Long> {
    @Query("""
          select s from Seminar s where s.expoId =?1
        """)
    List<Seminar> findAllByExpoId(Long expoId);
    Optional<Seminar> findByExpoId(Long expoId);

    @Query("""
        select s from Seminar  s where s.id =?1
        """)
    Optional<Seminar> findSeminarById(Long id);

    @Query("""
        select s from Seminar  s where s.name =?1 and s.expoId =?2
        """)
    Optional<Seminar> findSeminarByNameAndExpoId(String name,Long expoId);

    @Query("""
           select s from Seminar s where s.expoId =?1 and s.capacity != s.numOfGuest
        """)
        Set<Seminar> findAvailableSeminars(Long expoId);
    @Query("""
          select count (*) from Seminar s where s.expoId =?1
        """)
    Integer totalSeminar(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
