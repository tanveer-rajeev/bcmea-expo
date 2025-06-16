package com.betafore.evoting.VIP_GuestManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.ParticipantManagement.Participant;
import com.betafore.evoting.ParticipantManagement.ParticipantRepository;
import com.betafore.evoting.other_services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class VIP_GuestService implements VIP_GuestDao {

    private final VIP_GuestRepository vip_guestRepository;
    private final FileStorageService fileStorageServiceImp;
    private final ExpoRepository expoRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public List<VIP_Guest> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return vip_guestRepository.findAllByExpoId(expoId);
    }

    @Override
    public VIP_Guest findById(Long id) throws CustomException {
        Optional<VIP_Guest> optionalVIP_Guest = vip_guestRepository.findVIP_GuestById(id);
        if (optionalVIP_Guest.isEmpty())
            throw new CustomException("VIP_Guest not created by the given id: " + id);
        return optionalVIP_Guest.get();
    }

    public List<VIP_Guest> findByParticipantIdAndExpoId(Long participantId) throws CustomException {
        return participantRepository.findById(participantId).orElseThrow(() -> new CustomException("Participant not found"))
            .getVipGuestList();
    }

    @Override
    @Transactional
    public VIP_Guest save(VIP_GuestDto vipGuestDto, Long participantId, Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        Participant participant = participantRepository.findByExpoIdAndId(expoId, participantId).orElseThrow(() -> new CustomException("Participant not found"));
        if (!participant.getExpoId().equals(expoId)) {
            throw new CustomException("The participant not from this expo");
        }
        if (Objects.equals(participant.getVipGuestLimit(), participant.getActiveGuestCount())) {
            throw new CustomException("Creation of VIP guest limit exceeded for this participant");
        }

        Optional<VIP_Guest> optionalVIPGuest = vip_guestRepository.
            findVIP_GuestByExpoIdAndPhoneNumberOrEmail(expoId, vipGuestDto.getPhoneNumber(), vipGuestDto.getEmail());

        if (optionalVIPGuest.isPresent()) {
            throw new CustomException("Email or phone number already exist, try another one");
        }

        VIP_Guest vip_guest = VIP_Guest.builder()
            .expoId(expoId)
            .name(vipGuestDto.getName())
            .email(vipGuestDto.getEmail()).company(vipGuestDto.getCompany())
            .country(vipGuestDto.getCountry()).profession(vipGuestDto.getProfession())
            .phoneNumber(vipGuestDto.getPhoneNumber())
            .build();

        if (vipGuestDto.getImg() != null && !vipGuestDto.getImg().isEmpty()) {
            String img = fileStorageServiceImp.saveFile(vipGuestDto.getImg());
            vip_guest.setImg(img);
        }
        try {
            vip_guest.setParticipant(participant);
            participant.setActiveGuestCount(participant.getActiveGuestCount() == null ? 1 : participant.getActiveGuestCount() + 1);
            VIP_Guest savedVIPGuest = vip_guestRepository.save(vip_guest);
            return savedVIPGuest;
        } catch (Exception e) {
            log.error("Error saving VIP guest", e);
            throw new CustomException("Error saving VIP guest" + e.getMessage());
        }

    }

    @Transactional
    public void updateVIP_Guest(Long id, VIP_GuestDto vipGuestDto) throws CustomException {
        VIP_Guest vip_guest = findById(id);

        if (vipGuestDto.getName() != null && !vipGuestDto.getName().isEmpty() && !vipGuestDto.getName().equals(vip_guest.getName())) {
            vip_guest.setName(vipGuestDto.getName());
        }
        if (vipGuestDto.getPhoneNumber() != null && !vipGuestDto.getPhoneNumber().isEmpty() && !vipGuestDto.getPhoneNumber().equals(vip_guest.getPhoneNumber())) {
            Optional<VIP_Guest> vipGuestByPhoneNumberAndExpoId = vip_guestRepository
                .findVIP_GuestByPhoneNumberAndExpoId(vipGuestDto.getPhoneNumber(), vip_guest.getExpoId());
            if (vipGuestByPhoneNumberAndExpoId.isPresent()) {
                throw new CustomException("Phone number already present in the expo");
            }
            vip_guest.setPhoneNumber(vipGuestDto.getPhoneNumber());
        }
        if (vipGuestDto.getEmail() != null && !vipGuestDto.getEmail().isEmpty()
            && !vipGuestDto.getEmail().equals(vip_guest.getEmail())) {
            Optional<VIP_Guest> optionalVIPGuest = vip_guestRepository
                .findVIP_GuestByEmailAndExpoId(vipGuestDto.getEmail(), vip_guest.getExpoId());
            if (optionalVIPGuest.isPresent()) {
                throw new CustomException("Email already present in the expo");
            }
            vip_guest.setEmail(vipGuestDto.getEmail());
        }
        if (vipGuestDto.getCompany() != null && !vipGuestDto.getCompany().isEmpty() && !vipGuestDto.getCompany().equals(vip_guest.getCompany()))
            vip_guest.setCompany(vipGuestDto.getCompany());
        if (vipGuestDto.getProfession() != null && !vipGuestDto.getProfession().isEmpty() && !vipGuestDto.getProfession().equals(vip_guest.getProfession()))
            vip_guest.setProfession(vipGuestDto.getProfession());
        if (vipGuestDto.getCountry() != null && !vipGuestDto.getCountry().isEmpty() && !vipGuestDto.getCountry().equals(vip_guest.getCountry()))
            vip_guest.setCountry(vipGuestDto.getCountry());

        if (vipGuestDto.getImg() != null && !vipGuestDto.getImg().isEmpty())
            vip_guest.setImg(fileStorageServiceImp.saveFile(vipGuestDto.getImg()));

    }

    @Override
    public VIP_Guest saveAndFlush(VIP_Guest t, Long id) throws CustomException {
        Optional<VIP_Guest> optionalVIPGuest = vip_guestRepository.findVIP_GuestById(id);
        if (optionalVIPGuest.isEmpty()) {
            throw new CustomException("User not found by given id: " + id);
        }

        VIP_Guest vipGuest = optionalVIPGuest.get();
        return vip_guestRepository.save(VIP_Guest.builder().name(vipGuest.getName())
            .phoneNumber(vipGuest.getPhoneNumber())
            .profession(vipGuest.getProfession())
            .email(vipGuest.getEmail())
            .country(vipGuest.getCountry())
            .company(vipGuest.getCompany())
            .build());

    }

    @Override
    @Transactional
    public void removeById(Long id) throws CustomException {
        VIP_Guest vipGuest = vip_guestRepository.findVIP_GuestById(id).orElseThrow(() -> new CustomException("Vip guest not found by given id"));
        Participant participant = vipGuest.getParticipant();
        participant.setActiveGuestCount(participant.getActiveGuestCount() - 1);
        vip_guestRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        vip_guestRepository.deleteAllByExpoId(expoId);
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    public String createUriForVip(Long participantId, Long expoId, HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        URIBuilder u = new URIBuilder();
        u.setPath("/api/v1/vip/" + participantId + "/" + expoId + "?" + authHeader.substring(7));
        UriComponents authLink = ServletUriComponentsBuilder.
            fromCurrentContextPath().path(u.getPath()).build();
        return authLink.toUriString();
    }
}
