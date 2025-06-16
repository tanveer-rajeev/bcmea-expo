package com.betafore.evoting.GiftManagement;

import com.betafore.evoting.Exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCountService {

    private final GiftCountRepository giftCountRepository;

    public void deleteByGiftIdAndUserId(Long giftId, Long userId) {
        giftCountRepository.deleteByGiftIdAndUserId(giftId, userId);
    }

    @Transactional
    public void deleteAllByGiftId(Long giftId) {
        giftCountRepository.deleteAllByGiftId(giftId);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        giftCountRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void deleteAll(){
        giftCountRepository.deleteAll();
    }

    public GiftCount getGiftCount(Long id) throws CustomException {
        Optional<GiftCount> byGiftCountId = giftCountRepository.findByGiftCountId(id);
        if (byGiftCountId.isEmpty()) throw new CustomException("Gift count not present ");
        return byGiftCountId.get();
    }

    public GiftCount getGiftCountByUserIdAndGiftId(Long userID, Long giftId) throws CustomException {
        Optional<GiftCount> byGiftCountId = giftCountRepository.findByUserIdAndGiftId(userID, giftId);
        if (byGiftCountId.isEmpty()) throw new CustomException("Gift count not present ");
        return byGiftCountId.get();
    }

    public boolean isExistGiftCountByUserIdAndGiftId(Long userID, Long giftId) throws CustomException {
        Optional<GiftCount> byGiftCountId = giftCountRepository.findByUserIdAndGiftId(userID, giftId);

        return byGiftCountId.isPresent();
    }

    public void saveAll(List<GiftCount> giftCounts) {
        giftCountRepository.saveAll(giftCounts);
    }

    public void save(GiftCount giftCount) {
        giftCountRepository.save(giftCount);
    }

    @Transactional
    public void update(Long id, Long userId, Long giftId, Integer eligibility) throws CustomException {

        Optional<GiftCount> byGiftCountId = giftCountRepository.findByGiftCountId(id);
        if (byGiftCountId.isEmpty()) {
            throw new CustomException("Gift count id not present");
        }
        GiftCount giftCount = byGiftCountId.get();
        if (eligibility != null) {
            giftCount.setEligibility(eligibility);
        }
        if (userId != null && userId > 0) {
            giftCount.setUserId(userId);
        }
        if (giftId != null && giftId > 0) {
            giftCount.setGiftId(giftId);
        }
    }

    public void delete(Long id) {
        giftCountRepository.deleteById(id);
    }
}
