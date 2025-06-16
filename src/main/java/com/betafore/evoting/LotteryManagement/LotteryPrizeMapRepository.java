package com.betafore.evoting.LotteryManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotteryPrizeMapRepository extends JpaRepository<LotteryPrizeMap,Long> {

    Optional<LotteryPrizeMap> findByLotteryIdAndPosition(Long lotteryId,Integer position);

    List<LotteryPrizeMap> findAllByLotteryIdOrderByPosition(Long lotteryId);

    Optional<LotteryPrizeMap> findByPosition(Integer position);
}
