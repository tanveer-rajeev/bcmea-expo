package com.betafore.evoting.WinnerManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.LotteryManagement.Lottery;
import com.betafore.evoting.LotteryManagement.LotteryRepository;
import com.betafore.evoting.UserManagement.User;
import com.betafore.evoting.UserManagement.UserRepository;
import com.betafore.evoting.UserManagement.UserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class WinnerService implements WinnerDao {

    private final WinnerRepository winnerRepository;
    private final LotteryRepository lotteryRepository;
    private final ExpoRepository expoRepository;
    private final UserRepository userRepository;

    public UserRequestDto getWinner(Long expoId, Long lotteryId) throws CustomException {
        checkValidExpoId(expoId);
        try {
            UserRequestDto[] userRequestDto = new ObjectMapper()
                .readValue(new File("users" + expoId + ".text"), UserRequestDto[].class);
            List<UserRequestDto> list = new java.util.ArrayList<>(Arrays.stream(userRequestDto).toList());

            if (list.isEmpty()) throw new CustomException("User not present in the database");

            while (true) {
                Collections.shuffle(list);
                UserRequestDto user = list.subList(0, 1).get(0);

                Optional<Winner> optionalWinner = winnerRepository.findByUserIdAndLotteryId(user.getId(), lotteryId);

                if (optionalWinner.isEmpty()) {
                    return user;
                } else list.remove(user);

                if (list.isEmpty()) {
                    throw new CustomException("All winner has been selected,Please start lottery again");
                }
            }

        } catch (IOException e) {
            throw new CustomException(e.getMessage() + "Please start the Lottery to generate the user file");
        }
    }

    public Set<UserRequestDto> getPossibleWinnerList(Long expoId, Long lotteryId) throws CustomException, IOException {
        List<Winner> allByLotteryId = winnerRepository.findAllByLotteryId(lotteryId);
        if (allByLotteryId.isEmpty()) throw new CustomException("Winner not select yet");

        UserRequestDto[] userRequestDto = new ObjectMapper()
            .readValue(new File("users" + expoId + ".text"), UserRequestDto[].class);
        Set<UserRequestDto> totalLotteryUserList = Arrays.stream(userRequestDto).collect(Collectors.toSet());
        Set<Long> userIdList = allByLotteryId.stream().map(Winner::getUserId).collect(Collectors.toSet());

        return totalLotteryUserList.stream()
            .filter(user -> !userIdList.contains(user.getId()))
            .collect(Collectors.toSet());
    }

    @Override
    public Winner save(WinnerDto winnerDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        User user = userRepository.findById(winnerDto.getUserId()).orElseThrow(() -> new CustomException("User not found by given id: " + winnerDto.getUserId()));
        Optional<Winner> optionalWinner = winnerRepository.findByUserIdAndLotteryId(user.getId(), winnerDto.getLotteryId());

        if (optionalWinner.isPresent()) {
            throw new CustomException("The winner already selected for this lottery");
        }
        Lottery lottery = lotteryRepository.findByExpoIdAndId(expoId, winnerDto.getLotteryId())
            .orElseThrow(() -> new CustomException("Lottery not present by given expo id or lottery id"));

        Winner winner = Winner.builder()
            .expoId(expoId)
            .userId(winnerDto.getUserId())
            .placement(winnerDto.getPlacement())
            .lotteryId(winnerDto.getLotteryId())
            .build();
        lottery.addWinner(winner);
        return winnerRepository.save(winner);
    }

    @Override
    public Set<Winner> all(Long expoId, Long lotteryId) throws CustomException {
        checkValidExpoId(expoId);
        Lottery lottery = lotteryRepository.findByExpoIdAndId(expoId, lotteryId)
            .orElseThrow(() -> new CustomException("Lottery not present by given expo id or lottery id"));

        return lottery.getWinnerSet();
    }

    @Override
    public Winner saveAndFlush(WinnerDto winnerDto, Long id) throws CustomException {
        return winnerRepository.findById(id).map(winner -> {
            winner.setUserId(winnerDto.getUserId());
            winner.setPlacement(winnerDto.getPlacement());
            winner.setLotteryId(winnerDto.getLotteryId());
            return winnerRepository.save(winner);
        }).orElseThrow(() -> new CustomException("Winner not found"));
    }

    @Override
    public void removeById(Long id) throws CustomException {
        Winner winner = winnerRepository.findById(id).orElseThrow(() -> new CustomException("Winner not found by given id"));
        List<Lottery> all = lotteryRepository.findAll();
        all.forEach(lottery -> lottery.removeWinner(winner));
        winnerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<Lottery> all = lotteryRepository.findAllByExpoId(expoId);
        if (!all.isEmpty()) {
            all.forEach(lottery -> lottery.getWinnerSet().clear());
        }
        winnerRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
