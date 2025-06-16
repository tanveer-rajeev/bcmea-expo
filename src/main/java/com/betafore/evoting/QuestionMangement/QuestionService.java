package com.betafore.evoting.QuestionMangement;

import java.util.List;
import java.util.Optional;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.HallManagement.HallRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository repository;
    private final ExpoRepository expoRepository;
    private final HallRepository hallRepository;


    public List<Question> getAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);

        return repository.findAllByExpoId(expoId);
    }

    public Optional<Question> getQuestionByQuestion(String question) {
        return repository.findQuestionByQuestion(question);
    }

    public Optional<Question> getByQuestionAndExpoId(String question, Long expoId) throws CustomException {
        checkValidExpoId(expoId);

        return repository.findByQuestionAndExpoId(question, expoId);
    }

    public void save(Question entity) {
        repository.save(entity);
    }

    public Optional<Question> getById(Long id) {
        return repository.findById(id);
    }

    public Question getQuestionById(Long id) throws CustomException {
        return repository.findById(id).orElseThrow(() -> new CustomException("Question not found by given id"));
    }

    public void deleteById(Long id) throws CustomException {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllByExpoId(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        repository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
