package com.betafore.evoting.PrizeMenagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrizeService implements PrizeDao {

    private final PrizeRepository prizeRepository;
    private final FileStorageServiceImp fileStorageServiceImp;
    private final ExpoRepository expoRepository;

    @Override
    public List<PrizeResponse> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<Prize> allByExpoId = prizeRepository.findAllByExpoId(expoId);
        List<PrizeResponse> response = new ArrayList<>();
        for (Prize prize : allByExpoId) {
            response.add(getPrizeById(prize.getId()));
        }
        return response;
    }


    public PrizeResponse getPrizeById(Long id) throws CustomException {
        Optional<Prize> optionalPrize = prizeRepository.findById(id);
        if (optionalPrize.isEmpty()) {
            throw new CustomException("Prize not found by given id: " + id);
        }
        Prize prize = optionalPrize.get();
        PrizeResponse prizeResponse = PrizeResponse.builder()
            .id(prize.getId())
            .expoId(prize.getExpoId())
            .name(prize.getName())
            .type(prize.getType())
            .build();
        if (prize.getImg() != null && !prize.getImg().isEmpty()) {
            prizeResponse.setImg("/api/v1/prizes/img/" + prize.getId());
        }
        return prizeResponse;
    }

    @Override
    public Prize findById(Long id) throws CustomException {
        Optional<Prize> optionalPrize = prizeRepository.findById(id);
        if (optionalPrize.isEmpty()) {
            throw new CustomException("Prize not found by given id: " + id);
        }
        return optionalPrize.get();
    }

    @Override
    public Prize save(PrizeDto prizeDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Optional<Prize> optionalPrize = prizeRepository.findByNameAndExpoId(prizeDto.getName(), expoId);
        if (optionalPrize.isPresent()) {
            throw new CustomException("Prize name already present in the expo");
        }
        Prize prize = Prize.builder()
            .expoId(expoId)
            .name(prizeDto.getName())
            .type(prizeDto.getType())
            .build();

        if (prizeDto.getImg() != null && !prizeDto.getImg().isEmpty()) {
            String img = fileStorageServiceImp.saveFile(prizeDto.getImg());
            prize.setImg(img);
        }

        return prizeRepository.save(prize);
    }

    @Override
    @Transactional
    public void update(PrizeDto prizeDto, Long id) throws CustomException {
        Prize prize = findById(id);
        if (prizeDto.getName() != null && !prizeDto.getName().equals(prize.getName())) {
            Optional<Prize> optionalPrize = prizeRepository.findByNameAndExpoId(prizeDto.getName(), prize.getExpoId());
            if (optionalPrize.isPresent()) {
                throw new CustomException("Prize name already present in the expo");
            }
            prize.setName(prizeDto.getName());
        }
        if (prizeDto.getType() != null && !prizeDto.getType().equals(prize.getType())) {
            prize.setType(prizeDto.getType());
        }
        if (prizeDto.getImg() != null && !prizeDto.getImg().isEmpty()) {
            prize.setImg(fileStorageServiceImp.saveFile(prizeDto.getImg()));
        }

    }

    @Override
    public void removeById(Long id) throws CustomException {
        prizeRepository.findById(id).orElseThrow(() -> new CustomException("Prize not found"));
        prizeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        prizeRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

}
