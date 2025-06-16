package com.betafore.evoting.LotteryManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.PrizeMenagement.Prize;
import com.betafore.evoting.PrizeMenagement.PrizeRepository;
import com.betafore.evoting.UserManagement.UserRequestDto;
import com.betafore.evoting.UserManagement.UserRepository;
import com.betafore.evoting.Common.ResponseMessageConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.LOTTERY_CREATED;

@RequiredArgsConstructor
@Service
public class LotteryService implements LotteryDao {

    private final LotteryRepository lotteryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ExpoRepository expoRepository;
    private final PrizeRepository prizeRepository;
    private final LotteryPrizeMapRepository lotteryPrizeMapRepository;

    public List<Lottery> getAllRunningLottery(Long expoId) throws CustomException {
        checkValidExpoId(expoId);

        return lotteryRepository.findAllRunningLottery(expoId);
    }

    @Transactional
    public void startLottery(Long lotteryId) throws CustomException {
        Lottery lottery = findById(lotteryId);

        List<UserRequestDto> attendentUserList = userRepository
            .findAllAttendedUser(lottery.getExpoId())
            .stream()
            .map(user -> modelMapper.map(user, UserRequestDto.class))
            .toList();

        lottery.setState(ResponseMessageConstants.LOTTERY_RUNNING);

        try {
            new ObjectMapper().writeValue(new File("users" + lottery.getExpoId() + ".text"), attendentUserList);
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }

    }

    @Transactional
    public void endLottery(Long lotteryId) throws CustomException {
        Lottery lottery = findById(lotteryId);
        lottery.setState(ResponseMessageConstants.LOTTERY_FINISHED);
        try {
            Files.deleteIfExists(
                Paths.get("users" + lottery.getExpoId() + ".text")
            );
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public List<Lottery> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return lotteryRepository.findAllByExpoId(expoId);
    }

    @Override
    public Lottery findById(Long id) throws CustomException {
        Optional<Lottery> optionalLottery = lotteryRepository.findById(id);
        if (optionalLottery.isEmpty()) {
            throw new CustomException("Lottery not found by given id: " + id);
        }
        return optionalLottery.get();
    }

    public Prize findLotteryPrizeByPosition(Long lotteryId, Integer position) throws CustomException {
        LotteryPrizeMap lotteryPrizeMap = lotteryPrizeMapRepository.findByLotteryIdAndPosition(lotteryId, position)
            .orElseThrow(() -> new CustomException("Prize not found in the lottery by given position"));
        return prizeRepository.findById(lotteryPrizeMap.getPrizeId()).orElseThrow(() -> new CustomException("Prize not found"));
    }

    @Override
    public Lottery save(LotteryDto lotteryDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Lottery lottery = Lottery.builder().expoId(expoId).eventName(lotteryDto.getEventName())
            .prizeCount(lotteryDto.getPrizeCount())
            .state(LOTTERY_CREATED).build();

        return lotteryRepository.save(lottery);
    }

    @Transactional
    public LotteryPrizeMap addPrizeIntoLottery(LotteryPrizeMap lotteryPrizeMapDto) throws CustomException {
        findById(lotteryPrizeMapDto.getLotteryId());
        prizeRepository.findById(lotteryPrizeMapDto.getPrizeId()).orElseThrow(() -> new CustomException("Prize not found"));
        Optional<LotteryPrizeMap> byLotteryIdAndPosition = lotteryPrizeMapRepository.findByLotteryIdAndPosition(lotteryPrizeMapDto.getLotteryId(), lotteryPrizeMapDto.getPosition());

        if (byLotteryIdAndPosition.isPresent()) {
            throw new CustomException("The " + byLotteryIdAndPosition.get().getPosition() + " position has assigned with a prize , the prize id is " + byLotteryIdAndPosition.get().getPrizeId());
        }
        if (lotteryPrizeMapDto.getPosition() > 1) {
            Optional<LotteryPrizeMap> byLotteryIdAndPositionSeq =
                getLotteryPrizeMapByLotteryIdAndPosition(lotteryPrizeMapDto.getLotteryId(), lotteryPrizeMapDto.getPosition() - 1);
            if (byLotteryIdAndPositionSeq.isEmpty()) {
                throw new CustomException("The previous position has not filled up with a prize yet. Please fill up the previous position first");
            }
        }
        LotteryPrizeMap lotteryPrizeMap = LotteryPrizeMap.builder().prizeId(lotteryPrizeMapDto.getPrizeId())
            .lotteryId(lotteryPrizeMapDto.getLotteryId())
            .position(lotteryPrizeMapDto.getPosition())
            .build();
        return lotteryPrizeMapRepository.save(lotteryPrizeMap);
    }

    private Prize getPrizeById(Long id) throws CustomException {
        return prizeRepository.findById(id).orElseThrow(() -> new CustomException("Prize not found"));
    }

    private Optional<LotteryPrizeMap> getLotteryPrizeMapByLotteryIdAndPosition(Long lotteryId, Integer position) {
        return lotteryPrizeMapRepository.findByLotteryIdAndPosition(lotteryId, position);

    }

    @Transactional
    public void addAllPrizeIntoLottery(LotteryPrizeMapDto lotteryPrizeMapDto) throws CustomException {
        List<LotteryPrizeMap> lotteryPrizeMapList = new ArrayList<>();
        for (LotteryPrizeMap lotteryPrizeMap : lotteryPrizeMapDto.getLotteryPrizeMapSet()) {


            lotteryRepository.findById(lotteryPrizeMap.getLotteryId()).orElseThrow(() -> new CustomException("Lottery not found"));
            getPrizeById(lotteryPrizeMap.getPrizeId());

            Optional<LotteryPrizeMap> byLotteryIdAndPosition = getLotteryPrizeMapByLotteryIdAndPosition(lotteryPrizeMap.getLotteryId(), lotteryPrizeMap.getPosition());
            if (byLotteryIdAndPosition.isPresent()) {
                throw new CustomException("The " + byLotteryIdAndPosition.get().getPosition() + " position has assigned with a prize , the prize id is " + byLotteryIdAndPosition.get().getPrizeId());
            }

            LotteryPrizeMap alotteryPrizeMap = LotteryPrizeMap.builder().prizeId(lotteryPrizeMap.getPrizeId())
                .lotteryId(lotteryPrizeMap.getLotteryId())
                .position(lotteryPrizeMap.getPosition())
                .build();
            lotteryPrizeMapList.add(alotteryPrizeMap);
        }
        lotteryPrizeMapRepository.saveAll(lotteryPrizeMapList);
    }

    @Transactional
    public void updateLotteryPrizeMap(Long id, LotteryPrizeMap lotteryPrizeMapDto) throws CustomException {
        LotteryPrizeMap lotteryPrizeMap = lotteryPrizeMapRepository.findById(id).orElseThrow(() -> new CustomException("The lottery prize map not found by given id"));

        if (lotteryPrizeMapDto.getLotteryId() != null && !lotteryPrizeMapDto.getLotteryId().equals(lotteryPrizeMap.getLotteryId())) {
            lotteryPrizeMap.setLotteryId(lotteryPrizeMapDto.getLotteryId());
        }
        if (lotteryPrizeMapDto.getPrizeId() != null && !lotteryPrizeMapDto.getPrizeId().equals(lotteryPrizeMap.getPrizeId())) {
            lotteryPrizeMap.setPrizeId(lotteryPrizeMapDto.getPrizeId());
        }
        if (lotteryPrizeMapDto.getPosition() != null && !lotteryPrizeMapDto.getPosition().equals(lotteryPrizeMap.getPosition())) {
            if (lotteryPrizeMap.getPosition() > 1) {
                Optional<LotteryPrizeMap> previousPosition = getLotteryPrizeMapByLotteryIdAndPosition(lotteryPrizeMapDto.getLotteryId(), lotteryPrizeMap.getPosition() - 1);

                if (previousPosition.isEmpty()) {
                    throw new CustomException("The previous position has not filled up with a prize yet. Please fill up the previous position first");
                }
            }
            lotteryPrizeMap.setPosition(lotteryPrizeMapDto.getPosition());
        }
    }

    @Transactional
    public void updateAllLotteryPrizeMap(LotteryPrizeMapDto lotteryPrizeMapDtoList) throws CustomException {
        int index = 0;
        List<Long> ids = lotteryPrizeMapDtoList.getIds();
        for (LotteryPrizeMap lotteryPrizeMapDto : lotteryPrizeMapDtoList.getLotteryPrizeMapSet()) {

            LotteryPrizeMap lotteryPrizeMap = lotteryPrizeMapRepository.findById(ids.get(index++)).orElseThrow(() -> new CustomException("The lottery prize map not found by given id"));

            if (lotteryPrizeMapDto.getLotteryId() != null && !lotteryPrizeMapDto.getLotteryId().equals(lotteryPrizeMap.getLotteryId())) {
                lotteryPrizeMap.setLotteryId(lotteryPrizeMapDto.getLotteryId());
            }
            if (lotteryPrizeMapDto.getPrizeId() != null && !lotteryPrizeMapDto.getPrizeId().equals(lotteryPrizeMap.getPrizeId())) {
                lotteryPrizeMap.setPrizeId(lotteryPrizeMapDto.getPrizeId());
            }
            if (lotteryPrizeMapDto.getPosition() != null && !lotteryPrizeMapDto.getPosition().equals(lotteryPrizeMap.getPosition())) {
                if (lotteryPrizeMap.getPosition() > 1) {
                    Optional<LotteryPrizeMap> previousPosition = getLotteryPrizeMapByLotteryIdAndPosition(lotteryPrizeMapDto.getLotteryId(), lotteryPrizeMap.getPosition() - 1);

                    if (previousPosition.isEmpty()) {
                        throw new CustomException("The previous position has not filled up with a prize yet. Please fill up the previous position first");
                    }
                }
                lotteryPrizeMap.setPosition(lotteryPrizeMapDto.getPosition());
            }
        }
    }

    @Transactional
    public List<LotteryPrizeMap> getAllLotteryPrizeMapByLotteryId(Long lotteryId) {
        return lotteryPrizeMapRepository.findAllByLotteryIdOrderByPosition(lotteryId);
    }

    @Override
    @Transactional
    public void saveAndFlush(LotteryDto lotteryDto, Long id) throws CustomException {
        Lottery lottery = lotteryRepository.findById(id).orElseThrow(() -> new CustomException("Lottery not found"));

        if (lotteryDto.getEventName() != null && !lotteryDto.getEventName().isEmpty()
            && !lotteryDto.getEventName().equals(lottery.getEventName())) {
            lottery.setEventName(lotteryDto.getEventName());
        }

        if (lotteryDto.getPrizeCount() != null && !lotteryDto.getPrizeCount().equals(lottery.getPrizeCount())) {
            lottery.setPrizeCount(lotteryDto.getPrizeCount());
        }

    }

    @Transactional
    public void removePrizeFromLottery(Long id) throws CustomException {
        lotteryPrizeMapRepository.findById(id).orElseThrow(() -> new CustomException("Lottery prize map not found"));
        lotteryPrizeMapRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeById(Long id) throws CustomException {
        Lottery lottery = lotteryRepository.findById(id).orElseThrow(() -> new CustomException("Lottery not found by given id"));
        endLottery(lottery.getId());
        lotteryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        try {
            Files.deleteIfExists(
                Paths.get("users" + expoId + ".text")
            );
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
        lotteryPrizeMapRepository.deleteAll();
        lotteryRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
