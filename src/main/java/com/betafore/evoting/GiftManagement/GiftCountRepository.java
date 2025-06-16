package com.betafore.evoting.GiftManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCountRepository extends JpaRepository<GiftCount,Long> {
    Optional<GiftCount> findByUserIdAndGiftId(Long userId, Long giftId);

    @Query("""
         select g from GiftCount  g where g.id =?1
        """)
    Optional<GiftCount> findByGiftCountId(Long id);

    void deleteByGiftIdAndUserId(Long giftId,Long userId);

    void deleteAllByGiftId(Long giftId);
    void deleteAllByUserId(Long userId);
}
