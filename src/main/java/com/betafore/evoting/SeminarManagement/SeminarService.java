package com.betafore.evoting.SeminarManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.UserManagement.User;
import com.betafore.evoting.UserManagement.UserRepository;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SeminarService implements SeminarDao {

    private final SeminarRepository seminarRepository;
    private final UserRepository userRepository;
    private final FileStorageServiceImp fileStorageServiceImp;
    private final ExpoRepository expoRepository;

    @Override
    public List<Seminar> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return seminarRepository.findAllByExpoId(expoId);
    }

    @Override
    public Seminar findById(Long id) throws CustomException {
        Optional<Seminar> seminarById = seminarRepository.findSeminarById(id);
        if (seminarById.isEmpty()) throw new CustomException("Seminar not found by given id: " + id);
        return seminarById.get();
    }

    public Set<Seminar> getAvailableSeminars(Long expoId) {
        return seminarRepository.findAvailableSeminars(expoId);
    }

    @Override
    public Seminar save(SeminarDto seminarDto, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Optional<Seminar> optionalSeminar = seminarRepository.findSeminarByNameAndExpoId(seminarDto.getName(), expoId);

        if (optionalSeminar.isPresent()) {
            throw new CustomException("Seminar already present");
        }

        Seminar seminar = Seminar.builder()
            .expoId(expoId)
            .name(seminarDto.getName())
            .type(seminarDto.getType())
            .time(seminarDto.getTime())
            .duration(seminarDto.getDuration())
            .numOfGuest(seminarDto.getNumOfGuest())
            .capacity(seminarDto.getCapacity()).build();

        if (seminarDto.getImg() != null && !seminarDto.getImg().isEmpty()) {
            String img = fileStorageServiceImp.saveFile(seminarDto.getImg());
            seminar.setImg(img);
        }
        return seminarRepository.save(seminar);
    }

    @Override
    public Seminar saveAndFlush(Seminar t, Long id) {
        return null;
    }


    @Transactional
    public void update(Long id, SeminarDto seminarDto) throws CustomException {

        Seminar seminar = findById(id);
        if (seminarDto.getName() != null && !seminarDto.getName().isEmpty() && !Objects.equals(seminar.getName(), seminarDto.getName())) {
            Optional<Seminar> optionalSeminar =
                seminarRepository.findSeminarByNameAndExpoId(seminarDto.getName(), seminar.getExpoId());

            if (optionalSeminar.isPresent()) {
                throw new CustomException("Seminar already present");
            }
            seminar.setName(seminarDto.getName());
        }
        if (seminarDto.getType() != null && !seminarDto.getType().isEmpty() && !Objects.equals(seminar.getType(), seminarDto.getType()))
            seminar.setType(seminarDto.getType());
        if (seminarDto.getImg() != null && !seminarDto.getImg().isEmpty()) {
            seminar.setImg(fileStorageServiceImp.saveFile(seminarDto.getImg()));
        }
        if (seminarDto.getDuration() != null && seminarDto.getDuration() > 0 && !seminarDto.getDuration().equals(seminar.getDuration()))
            seminar.setDuration(seminarDto.getDuration());
        if (seminarDto.getNumOfGuest() != null && !seminarDto.getNumOfGuest().equals(seminar.getNumOfGuest()))
            seminar.setNumOfGuest(seminarDto.getNumOfGuest());
        if (seminarDto.getTime() != null && !seminarDto.getTime().isEmpty() && !Objects.equals(seminar.getTime(), seminarDto.getTime()))
            seminar.setTime(seminarDto.getTime());
        if (seminarDto.getCapacity() != null && seminarDto.getCapacity() > 0 && !Objects.equals(seminar.getCapacity(), seminarDto.getCapacity()))
            seminar.setCapacity(seminarDto.getCapacity());

    }

    @Override
    public void removeById(Long id) throws CustomException {
        Optional<Seminar> optionalSeminar = seminarRepository.findSeminarById(id);
        if (optionalSeminar.isEmpty()) throw new CustomException("seminar not found: " + id);

        Seminar seminar = optionalSeminar.get();

        List<User> users = userRepository.findAllByExpoId(seminar.getExpoId());
        for (User user : users) {
            user.getSeminars().remove(seminar);
        }

        seminarRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        List<User> users = userRepository.findAllByExpoId(expoId);
        List<Seminar> seminars = seminarRepository.findAllByExpoId(expoId);
        for (Seminar seminar : seminars) {
            if (!users.isEmpty()) {
                users.forEach(user -> user.getSeminars().remove(seminar));
            }
        }
        seminarRepository.deleteAllByExpoId(expoId);
    }

    public boolean seminarAccessVerification(Long seminarId, Long userId) throws CustomException {
        Optional<Seminar> optionalSeminar = seminarRepository.findSeminarById(seminarId);
        if (optionalSeminar.isEmpty())
            throw new CustomException("Seminar not found by given id: " + seminarId);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new CustomException("User not found by given id: " + userId);

        User user = optionalUser.get();
        Seminar seminar = optionalSeminar.get();

        return user.getSeminars().contains(seminar);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
