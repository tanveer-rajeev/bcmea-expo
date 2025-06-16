package com.betafore.evoting.VoteMangement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final VoteRepository repository;

    public List<VoteList> getAll(Long expoId) {
        return repository.findAllByExpoId(expoId);
    }

    public void submit(List<Vote> entity) {
        repository.saveAll(entity);
    }

    public Optional<Vote> getById(Long id) {
        return repository.findById(id);
    }

    public List<VoteResult> voteCalculation(Long expoId) {
        return repository.voteCalculation(expoId);
    }

    public List<VoteResult> resultOfAHall(String hallSlug) {
        return repository.resultOfAHall(hallSlug);
    }

    public List<VoteResult> resultOfAStall(String stallSlug) {
        return repository.resultOfAStall(stallSlug);
    }

    public List<VoteResult> resultOfAHallAndStall(String hallSlug, String stallSlug) {
        return repository.resultOfAHallAndStall(hallSlug, stallSlug);
    }

    public List<VoteList> filterResultByHall(String hallSlug) {
        return repository.findAllByHall_Slug(hallSlug);
    }

    public List<VoteList> filterResultByStall(String stallSlug) {
        return repository.findAllByStall_Slug(stallSlug);
    }

    public List<VoteList> filterResultByQuestion(Long questionId) {
        return repository.findAllByQuestion_Id(questionId);
    }

    public List<VoteList> filterResultByHallStallQuestion(String hallSlug, String stallSlug, Long questionId) {
        return repository.findAllByHall_SlugAndStall_SlugAndQuestion_Id(hallSlug, stallSlug, questionId);
    }

    public List<VoteList> filterResultByHallStall(String hallSlug, String stallSlug) {
        return repository.findAllByHall_SlugAndStall_Slug(hallSlug, stallSlug);
    }

    public List<VoteList> filterResultByHallQuestion(String hallSlug, Long questionId) {
        return repository.findAllByHall_SlugAndQuestion_Id(hallSlug, questionId);
    }

    public List<VoteList> filterResultByStallQuestion(String stallSlug, Long questionId) {
        return repository.findAllByStall_SlugAndQuestion_Id(stallSlug, questionId);
    }

    public List<VoteList> analytics(Long expoId) {
        return repository.analytics(expoId);
    }

    public List<VoteList> filterAnalyticsByHall(String hallSlug,Long expoId) {
        return repository.filterAnalyticsByHall(hallSlug,expoId);
    }

    public boolean eligibility(Long hallId, Long userId) {
        return repository.eligibility(hallId, userId);
    }

    @Transactional
    public void removeAll(Long expoId){
        repository.deleteAllByExpoId(expoId);
    }
}
