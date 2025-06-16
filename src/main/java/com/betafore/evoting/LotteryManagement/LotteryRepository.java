package com.betafore.evoting.LotteryManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {

    Lottery findByEventName(String eventName);

    @Query("""
          select count (*) from Lottery l where l.expoId =?1
        """)
    Integer totalLottery(Long expoId);

    @Query("""
        select l from Lottery l where l.state = "RUNNING" and l.expoId =?1
        """)
    List<Lottery> findAllRunningLottery(Long expoId);

    @Query("""
        select l from Lottery l where l.expoId =?1
        """)
    List<Lottery> findAllByExpoId(Long expoId);

    Optional<Lottery> findByExpoIdAndId(Long expoId, Long id);

    Optional<Lottery> findByExpoId(Long expoId);

    void deleteAllByExpoId(Long expoId);
}
