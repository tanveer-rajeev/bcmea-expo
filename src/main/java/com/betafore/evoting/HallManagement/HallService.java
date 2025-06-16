package com.betafore.evoting.HallManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.StallManagement.StallRepository;
import com.betafore.evoting.VoteMangement.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HallService {

    private final HallRepository repository;
    private final ExpoRepository expoRepository;

    public List<Hall> getAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return repository.findAllByExpoId(expoId);
    }

    public void save(Hall entity) {
        repository.save(entity);
    }

    public Optional<Hall> getById(Long id) {
        return repository.findById(id);
    }

    public boolean isNameAlreadyTaken(String name, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Optional<Hall> optionalHall = repository.findByNameAndExpoId(name, expoId);
        return optionalHall.isPresent();
    }

    public Optional<Hall> getByNameAndExpoId(String name, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return repository.findByNameAndExpoId(name, expoId);
    }

    public Optional<Hall> getByIdAndExpoId(Long id, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return repository.findByIdAndExpoId(id, expoId);
    }

    @Transactional
    public void deleteAllByExpoId(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        repository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    @Transactional
    public void deleteById(Long id) throws CustomException {
        Hall hall = getById(id).orElseThrow(() -> new CustomException("Hall not found by id"));
        if (!hall.getStalls().isEmpty() || !hall.getVotes().isEmpty() || !hall.getQuestions().isEmpty()) {
            throw new CustomException("One of the Stall or Vote is connected with the hall");
        }
        repository.deleteById(id);
    }
}
