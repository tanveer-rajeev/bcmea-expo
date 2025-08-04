package com.betafore.evoting.ParticipantManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ParticipantService implements ParticipantDao {

    private final ParticipantRepository participantRepository;
    private final OrganizerRepository organizerRepository;
    private final ExpoRepository expoRepository;

    @Override
    public Participant findById(Long id) throws CustomException {
        return participantRepository.findById(id).stream()
            .filter(participant -> participant.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new CustomException("Participant not found: " + id));
    }

    @Override
    public Participant save(Participant participant, Long organizerId, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        organizerRepository.findById(organizerId).orElseThrow(() -> new CustomException("Organizer not found by given id: " + organizerId));
        participant.setOrganizerId(organizerId);
        participant.setExpoId(expoId);
        return participantRepository.save(participant);
    }

    @Transactional
    public void updateParticipant(Participant participantDto, Long id) throws CustomException {
        Participant participant = findById(id);

        if (participantDto.getName() != null && !participantDto.getName().isEmpty()
            && !participantDto.getName().equals(participant.getName())) {
            participant.setName(participantDto.getName());
        }
        if (participantDto.getType() != null && !participantDto.getType().isEmpty()
            && !participantDto.getType().equals(participant.getType())) {
            participant.setType(participantDto.getType());
        }
        if (participantDto.getOrganizerId() != null && !participantDto.getOrganizerId().equals(participant.getOrganizerId())) {
            participant.setOrganizerId(participantDto.getOrganizerId());
        }
        if (participantDto.getVipGuestLimit() != null && !participantDto.getVipGuestLimit().equals(participant.getVipGuestLimit())) {
            participant.setVipGuestLimit(participantDto.getVipGuestLimit());
        }
    }

    @Override
    public List<Participant> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return participantRepository.findAllByExpoId(expoId);
    }

    @Override
    @Transactional
    public void removeById(Long id) throws CustomException {
        Participant participant = participantRepository.findById(id).orElseThrow(() -> new CustomException("Participant not found by given id"));
        if (!participant.getVipGuestList().isEmpty() || !participant.getEmployeeList().isEmpty()){
            throw new CustomException("One of the Employees or VIP is connected with the participant");
        }
            participantRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        participantRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
