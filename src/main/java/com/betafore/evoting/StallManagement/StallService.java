package com.betafore.evoting.StallManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.HallManagement.Hall;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StallService{

    private final StallRepository repository;

    public List<Stall> getAll(Long expoId) {
        return repository.findAllByExpoId(expoId);
    }

    public void save(Stall entity) {
        repository.save(entity);
    }

    public Optional<Stall> getBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    public Optional<Stall> getById(Long id) {
        return repository.findById(id);
    }
    public Stall findById(Long id) throws CustomException {

            Optional<Stall> optionalStall = repository.findById(id);
            if (optionalStall.isEmpty()) throw new CustomException("Stall not found by given id: " + id);
            return optionalStall.get();

    }
    public boolean isAlreadyTaken(String name){
        Optional<Stall> optionalHall = repository.findByName(name);
        return optionalHall.isPresent();
    }
    public void deleteBySlug(String slug) {
        repository.deleteBySlug(slug);
    }

    @Transactional
    public void deleteAllByExpoId(Long expoId) {
        repository.deleteAllByExpoId(expoId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Stall> findByNameAndExpoId(String name,Long expoId){
        return repository.findByNameAndExpoId(name,expoId);
    }
}
