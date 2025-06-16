package com.betafore.evoting.GiftManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.UserManagement.User;
import com.betafore.evoting.UserManagement.UserRepository;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GiftService implements GiftDao {


    private final GiftRepository giftRepository;
    private final FileStorageServiceImp fileStorageServiceImp;
    private final UserRepository userRepository;
    private final GiftCountService giftCountService;
    private final ExpoRepository expoRepository;

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    @Transactional
    public boolean giftScan(Long giftId, Long userId) throws CustomException {

        Optional<Gift> optionalGift = giftRepository.findGiftById(giftId);
        if (optionalGift.isEmpty()) {
            throw new CustomException("Gift not found by given name: " + giftId);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new CustomException("User not found by given id: " + userId);
        }

        Gift gift = optionalGift.get();
        User user = optionalUser.get();
        if (user.getTotalGiftCount() == 0 || !user.getGifts().contains(gift)) {
            return false;
        }

        GiftCount giftCount = giftCountService.getGiftCountByUserIdAndGiftId(userId, gift.getId());
        user.setTotalGiftCount(user.getTotalGiftCount() - 1);
        if (giftCount.getEligibility() == 1) {
            user.getGifts().remove(gift);
            giftCountService.delete(giftCount.getId());
        } else {
            giftCountService.update(giftCount.getId(), userId, gift.getId(), giftCount.getEligibility() - 1);
        }

        return true;
    }

    @Override
    public List<Gift> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return giftRepository.findByExpoId(expoId);
    }

    @Override
    public Gift findById(Long id) throws CustomException {
        Optional<Gift> optionalGift = giftRepository.findGiftById(id);
        if (optionalGift.isEmpty())
            throw new CustomException("Gift not created by the given id: " + id);

        return optionalGift.get();
    }

    @Override
    public Gift save(GiftDto giftDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Optional<Gift> optionalGift = giftRepository.findGiftByNameAndExpoId(giftDto.getName(), expoId);
        if (optionalGift.isPresent()) {
            throw new CustomException("Gift already present");
        }

        Gift gift = Gift.builder()
            .expoId(expoId)
            .name(giftDto.getName())
            .description(giftDto.getDescription())
            .eligibility(giftDto.getEligibility())
            .type(giftDto.getType())
            .isEligibleForAll(Boolean.parseBoolean(giftDto.getIsEligibleForAll()))
            .build();
        if (giftDto.getImg() != null && !giftDto.getImg().isEmpty()) {
            gift.setImg(fileStorageServiceImp.saveFile(giftDto.getImg()));
        }
        return giftRepository.save(gift);
    }

    @Transactional
    public void updateGift(Long id, GiftDto giftDto) throws CustomException {
        Gift gift = findById(id);

        if (giftDto.getName() != null && !giftDto.getName().isEmpty() && !giftDto.getName().equals(gift.getName()))
        {
            Optional<Gift> optionalGift = giftRepository.findGiftByNameAndExpoId(giftDto.getName(), gift.getExpoId());
            if (optionalGift.isPresent()) {
                throw new CustomException("Gift already present");
            }
            gift.setName(giftDto.getName());
        }
        if (giftDto.getType() != null && !giftDto.getType().isEmpty()) gift.setType(giftDto.getType());

        if (giftDto.getImg() != null && !giftDto.getImg().isEmpty()) {
            gift.setImg(fileStorageServiceImp.saveFile(giftDto.getImg()));
        }

        if (giftDto.getDescription() != null && !giftDto.getDescription().isEmpty() && !giftDto.getDescription().equals(gift.getDescription()))
            gift.setDescription(giftDto.getDescription());

        if (giftDto.getEligibility() != null && !giftDto.getEligibility().equals(gift.getEligibility()))
            gift.setEligibility(giftDto.getEligibility());

    }

    @Override
    public Gift saveAndFlush(GiftDto t, Long id) {
        return null;
    }

    @Override
    @Transactional
    public void removeById(Long id) throws CustomException {
        Gift gift = findById(id);
        if (gift.getImg() != null && !gift.getImg().isEmpty()) {
            if (fileStorageServiceImp.fileDoesExist(gift.getImg())) {
                fileStorageServiceImp.deleteFile(gift.getImg());
            }
        }
        List<User> users = userRepository.findAllByExpoId(gift.getExpoId());
        for (User user : users) {
            user.getGifts().remove(gift);
            giftCountService.deleteByGiftIdAndUserId(id, user.getId());
        }
        giftRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<Gift> gifts = giftRepository.findByExpoId(expoId);
        List<User> users = userRepository.findAllByExpoId(expoId);

        for (Gift gift : gifts) {
            if (gift.getImg() != null && !gift.getImg().isEmpty()) {
                if (fileStorageServiceImp.fileDoesExist(gift.getImg())) {
                    fileStorageServiceImp.deleteFile(gift.getImg());
                }
            }
            if (!users.isEmpty()) {
                for (User user : users) {
                    user.getGifts().remove(gift);
                }
            }
            giftCountService.deleteAllByGiftId(gift.getId());
        }
        giftRepository.deleteAllByExpoId(expoId);
    }

    @Transactional
    public void setEligibility(Long giftId, boolean eligible) throws CustomException {
        Gift gift = findById(giftId);
        gift.setEligibleForAll(eligible);
    }
}
