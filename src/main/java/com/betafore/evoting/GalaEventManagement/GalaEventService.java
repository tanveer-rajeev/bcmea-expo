package com.betafore.evoting.GalaEventManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.VIP_GuestManagement.VIP_Guest;
import com.betafore.evoting.VIP_GuestManagement.VIP_GuestRepository;
import com.betafore.evoting.other_services.FileStorageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GalaEventService implements GalaEventDao {

    private final GalaEventRepository galaEventRepository;
    private final VIP_GuestRepository vipGuestRepository;
    private final FileStorageServiceImp fileStorageServiceImp;
    private final ExpoRepository expoRepository;

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }

    @Override
    public List<GalaEvent> all(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return galaEventRepository.findAllByExpoId(expoId);
    }

    @Override
    public GalaEvent findById(Long id) throws CustomException {
        Optional<GalaEvent> galaEventById = galaEventRepository.findGalaEventById(id);
        if (galaEventById.isEmpty()) throw new CustomException("GalaEvent not found by given id: " + id);

        return galaEventById.get();
    }


    @Override
    public GalaEvent save(GalaEventDto galaEventDto, Long expoId) throws CustomException {

        checkValidExpoId(expoId);
        Optional<GalaEvent> optionalGalaEvent = galaEventRepository.findGalaEventByNameAndExpoId(galaEventDto.getName(), expoId);

        if (optionalGalaEvent.isPresent()) {
            throw new CustomException("Gala event name already present in the expo");
        }
        GalaEvent galaEvent = GalaEvent.builder()
            .expoId(expoId)
            .name(galaEventDto.getName())
            .type(galaEventDto.getType())
            .time(galaEventDto.getTime())
            .duration(galaEventDto.getDuration())
            .numOfGuest(galaEventDto.getNumOfGuest())
            .capacity(galaEventDto.getCapacity())
            .build();

        if (galaEventDto.getImg() != null && !galaEventDto.getImg().isEmpty()) {
            String img = fileStorageServiceImp.saveFile(galaEventDto.getImg());
            galaEvent.setImg(img);
        }

        return galaEventRepository.save(galaEvent);
    }

    @Override
    public GalaEvent saveAndFlush(GalaEvent t, Long id) {
        return null;
    }


    @Transactional
    public void update(Long id, GalaEventDto galaEventDto) throws CustomException {

        GalaEvent galaEvent = findById(id);
        if (galaEventDto.getName() != null && !galaEventDto.getName().isEmpty() && !Objects.equals(galaEvent.getName(), galaEventDto.getName())) {
            Optional<GalaEvent> optionalGalaEvent = galaEventRepository
                .findGalaEventByNameAndExpoId(galaEventDto.getName(), galaEvent.getExpoId());

            if (optionalGalaEvent.isPresent()) {
                throw new CustomException("Gala event name already present in the expo");
            }
            galaEvent.setName(galaEventDto.getName());
        }
        if (galaEventDto.getType() != null && !galaEventDto.getType().isEmpty() && !Objects.equals(galaEvent.getType(), galaEventDto.getType()))
            galaEvent.setType(galaEventDto.getType());

        if (galaEventDto.getImg() != null && !galaEventDto.getImg().isEmpty()) {
            galaEvent.setImg(fileStorageServiceImp.saveFile(galaEventDto.getImg()));
        }

        if (galaEventDto.getDuration() != null && galaEventDto.getDuration() > 0 && !galaEventDto.getDuration().equals(galaEvent.getDuration()))
            galaEvent.setDuration(galaEventDto.getDuration());
        if (galaEventDto.getNumOfGuest() != null && !galaEventDto.getNumOfGuest().equals(galaEvent.getNumOfGuest()))
            galaEvent.setNumOfGuest(galaEventDto.getNumOfGuest());
        if (galaEventDto.getTime() != null && !galaEventDto.getTime().isEmpty() && !Objects.equals(galaEvent.getTime(), galaEventDto.getTime()))
            galaEvent.setTime(galaEventDto.getTime());
        if (galaEventDto.getCapacity() != null && galaEventDto.getCapacity() > 0 && !Objects.equals(galaEvent.getCapacity(), galaEventDto.getCapacity()))
            galaEvent.setCapacity(galaEventDto.getCapacity());

    }

    @Override
    public void removeById(Long id) throws CustomException {
        galaEventRepository.findById(id).orElseThrow(() -> new CustomException("Gala event not found by given id"));
        galaEventRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) {
        galaEventRepository.deleteAllByExpoId(expoId);
    }

    @Transactional
    public boolean galaEventAccessVerification(Long galaId, Long userId) throws CustomException {
        GalaEvent galaEvent = galaEventRepository.findById(galaId).orElseThrow(() -> new CustomException("Gala event not found by given id"));
        if (galaEvent.getCapacity() == 0) {
            throw new CustomException("Entry limit exceeded");
        }
        Optional<VIP_Guest> optionalUser = vipGuestRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new CustomException("User not found by given id: " + userId);
        galaEvent.setCapacity(galaEvent.getCapacity() - 1);
        galaEvent.setNumOfGuest(galaEvent.getNumOfGuest() + 1);
        return true;
    }
}
