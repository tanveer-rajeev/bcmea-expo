package com.betafore.evoting.ExpoManagement;

import com.betafore.evoting.EmploymentManagement.EmployeeService;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoServiceManagement.ExpoService;
import com.betafore.evoting.ExpoServiceManagement.ExpoServiceDaoImpl;
import com.betafore.evoting.GalaEventManagement.GalaEventService;
import com.betafore.evoting.GiftManagement.GiftService;
import com.betafore.evoting.HallManagement.HallService;
import com.betafore.evoting.LotteryManagement.LotteryService;
import com.betafore.evoting.OrganizerManagement.OrganizerService;
import com.betafore.evoting.ParticipantManagement.ParticipantService;
import com.betafore.evoting.PrizeMenagement.PrizeService;
import com.betafore.evoting.QuestionMangement.QuestionService;
import com.betafore.evoting.ReportManagement.ReportService;
import com.betafore.evoting.RolePermissionManagement.RolePermissionService;
import com.betafore.evoting.SeminarManagement.SeminarService;
import com.betafore.evoting.StallManagement.StallService;
import com.betafore.evoting.OtpManagement.OtpRepository;
import com.betafore.evoting.UserManagement.UserService;
import com.betafore.evoting.VIP_GuestManagement.VIP_GuestService;
import com.betafore.evoting.VoteMangement.VoteService;
import com.betafore.evoting.WinnerManagement.WinnerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpoDaoImpl implements ExpoDao {

    private final ExpoRepository expoRepository;
    private final ExpoServiceDaoImpl expoServiceDaoImpl;
    private final OrganizerService organizerService;
    private final EmployeeService employeeService;
    private final GalaEventService galaEventService;
    private final GiftService giftService;
    private final SeminarService seminarService;
    private final LotteryService lotteryService;
    private final HallService hallService;
    private final QuestionService questionService;
    private final UserService userService;
    private final VIP_GuestService vipGuestService;
    private final StallService stallService;
    private final PrizeService prizeService;
    private final WinnerService winnerService;
    private final ReportService reportService;
    private final ParticipantService participantService;
    private final VoteService voteService;
    private final OtpRepository otpRepository;
    private final RolePermissionService rolePermissionService;


    public String createExpoAuthenticationUri(Long expoId) {
        URIBuilder u = new URIBuilder();
        u.setPath("/api/v1/auth/login/" + expoId);
        UriComponents authLink = ServletUriComponentsBuilder.
            fromCurrentContextPath().path(u.getPath()).build();
        return authLink.toUriString();
    }

    @Override
    public List<Expo> all() {
        return expoRepository.findAll();
    }

    public Set<ExpoService> getAllEnableServices(Long expoId) throws CustomException {
        Expo expo = findById(expoId);
        return expo.getExpoServiceSet().stream().filter(expoService -> expoService.getStatus().equals("enable"))
            .collect(Collectors.toSet());
    }

    @Override
    public Expo findById(Long id) throws CustomException {
        Optional<Expo> optionalExpo = expoRepository.findById(id);
        if (optionalExpo.isEmpty()) {
            throw new CustomException("Expo not found by given id: " + id);
        }
        return optionalExpo.get();
    }

    @Override
    public Expo save(ExpoDto expoDto) throws CustomException {
        Optional<Expo> optionalExpo = expoRepository.findByExpoName(expoDto.getExpoName());
        if (optionalExpo.isPresent()) {
            throw new CustomException("Expo name already present.Try new one");
        }

        Expo expo = Expo.builder().expoName(expoDto.getExpoName())
            .clientName(expoDto.getClientName())
            .status(expoDto.getStatus()).build();

        Set<ExpoService> expoServices = new HashSet<>();

        for (Long expoServiceId : expoDto.getExpoServices()) {
            ExpoService expoService = expoServiceDaoImpl.findEnableServiceById(expoServiceId);
            expoServices.add(expoService);
        }
        expo.setExpoServiceSet(expoServices);
        return expoRepository.save(expo);
    }

    @Transactional
    @Override
    public void saveAndFlush(ExpoDto expoDto, Long id) throws CustomException {
        Optional<Expo> optionalExpo = expoRepository.findById(id);
        if (optionalExpo.isEmpty()) {
            throw new CustomException("Expo not found by given id: ");
        }
        Set<ExpoService> expoServiceSet = new HashSet<>();

        Expo expo = optionalExpo.get();

        if (expoDto.getExpoName() != null && !expoDto.getExpoName().equals(expo.getExpoName())) {
            Optional<Expo> optionalExpoByName = expoRepository.findByExpoName(expoDto.getExpoName());
            if (optionalExpoByName.isPresent()) {
                throw new CustomException("Expo name already present.Try new one");
            }
            expo.setExpoName(expoDto.getExpoName());
        }

        if (expoDto.getStatus() != null && !expoDto.getStatus().equals(expo.getStatus())) {
            expo.setStatus(expoDto.getStatus());
        }

        if (expoDto.getClientName() != null && !expoDto.getClientName().equals(expo.getClientName())) {
            expo.setClientName(expoDto.getClientName());
        }

        if (!expoDto.getExpoServices().isEmpty()) {
            for (Long expoServiceId : expoDto.getExpoServices()
            ) {
                ExpoService expoService = expoServiceDaoImpl.findById(expoServiceId);
                expoServiceSet.add(expoService);
            }
            expo.setExpoServiceSet(expoServiceSet);
        }

    }

    @Transactional
    public void deleteServiceById(Long expoId, Long serviceId) throws CustomException {
        Expo expo = findById(expoId);
        Optional<ExpoService> first = expo.getExpoServiceSet().stream()
            .filter(expoService -> expoService.getId().equals(serviceId))
            .findFirst();
        if (first.isEmpty()) {
            throw new CustomException("Expo Service not found by given serviceId: " + serviceId);
        }
        ExpoService expoService = first.get();
        expo.getExpoServiceSet().remove(expoService);
    }

    public String isExpoServiceTaken(Long expoId, Long serviceId) throws CustomException {
        Expo expo = findById(expoId);
        Optional<ExpoService> first = expo.getExpoServiceSet().stream()
            .filter(expoService -> expoService.getId().equals(serviceId))
            .findFirst();
        if (first.isEmpty()) return "Service has not been taken yet";
        return "Service already has been taken";
    }

    @Transactional
    public void addExpoService(Long serviceId, Long expoId) throws CustomException {
        Expo expo = findById(expoId);
        ExpoService expoService = expoServiceDaoImpl.findEnableExpoServiceById(serviceId);
        expo.getExpoServiceSet().add(expoService);
    }

    @Override
    @Transactional
    public void removeById(Long expoId) throws CustomException {
        Optional<Expo> optionalExpo = expoRepository.findById(expoId);
        if (optionalExpo.isEmpty()) {
            throw new CustomException("Expo not found by given id");
        }
        voteService.removeAll(expoId);
        userService.removeAll(expoId);
        organizerService.removeAll(expoId);
        rolePermissionService.deleteAllRoleByExpoId(expoId);
        giftService.removeAll(expoId);
        seminarService.removeAll(expoId);
        employeeService.removeAll(expoId);
        galaEventService.removeAll(expoId);
        stallService.deleteAllByExpoId(expoId);
        hallService.deleteAllByExpoId(expoId);
        questionService.deleteAllByExpoId(expoId);
        lotteryService.removeAll(expoId);
        vipGuestService.removeAll(expoId);
        participantService.removeAll(expoId);
        prizeService.removeAll(expoId);
        reportService.removeAll(expoId);
        winnerService.removeAll(expoId);
        otpRepository.deleteAll();
        expoRepository.deleteById(expoId);
    }

}
