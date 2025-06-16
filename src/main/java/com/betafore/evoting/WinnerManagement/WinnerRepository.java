package com.betafore.evoting.WinnerManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinnerRepository extends JpaRepository<Winner,Long> {

    Optional<Winner> findByUserId(Long userId);
    Optional<Winner> findByUserIdAndLotteryId(Long id,Long expoId);

    List<Winner> findAllByLotteryId(Long id);
    void deleteAllByExpoId(Long expoId);
}
