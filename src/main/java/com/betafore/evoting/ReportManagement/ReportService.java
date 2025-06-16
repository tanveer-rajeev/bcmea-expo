package com.betafore.evoting.ReportManagement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.GiftManagement.GiftRepository;
import com.betafore.evoting.HallManagement.HallRepository;
import com.betafore.evoting.LotteryManagement.LotteryRepository;
import com.betafore.evoting.OrganizerManagement.OrganizerRepository;
import com.betafore.evoting.QuestionMangement.QuestionRepository;
import com.betafore.evoting.RolePermissionManagement.RoleRepository;
import com.betafore.evoting.SeminarManagement.SeminarRepository;
import com.betafore.evoting.StallManagement.StallRepository;
import com.betafore.evoting.UserManagement.UserRepository;
import com.betafore.evoting.VIP_GuestManagement.VIP_GuestRepository;
import com.betafore.evoting.VoteMangement.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements ReportDao {

    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final StallRepository stallRepository;
    private final QuestionRepository questionRepository;
    private final VoteRepository voteRepository;
    private final GiftRepository giftRepository;
    private final SeminarRepository seminarRepository;
    private final LotteryRepository lotteryRepository;
    private final VIP_GuestRepository vipGuestRepository;
    private final RoleRepository roleRepository;
    private final ReportRepository reportRepository;
    private final ExpoRepository expoRepository;

    @Override
    public ReportDto report(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        return ReportDto.builder()
            .expoId(expoId)
            .totalRegisteredGuest(userRepository.findTotalRegisteredUser(expoId))
            .totalHall(hallRepository.totalHall(expoId))
            .totalStaff(roleRepository.totalStaff(expoId))
            .totalQuestion(questionRepository.totalQuestion(expoId))
            .totalManagers(roleRepository.totalManager(expoId))
            .totalStall(stallRepository.totalStall(expoId))
            .totalVote(voteRepository.totalVote(expoId))
            .totalVoteHallPavilion(0)
            .totalGift(giftRepository.totalGift(expoId))
            .totalSeminar(seminarRepository.totalSeminar(expoId))
            .totalLottery(lotteryRepository.totalLottery(expoId))
            .totalVip_guest(vipGuestRepository.totalVIP_Guest(expoId))
            .build();
    }

    @Override
    public Report save(Long expoId) throws CustomException {
        ReportDto reportDto = report(expoId);
        Report report = Report.builder()
            .expoId(expoId)
            .registeredGuest(reportDto.getTotalRegisteredGuest())
            .hall(reportDto.getTotalHall())
            .question(reportDto.getTotalQuestion())
            .managers(reportDto.getTotalManagers())
            .staff(reportDto.getTotalStaff())
            .vote(reportDto.getTotalVote())
            .voteHallPavilion(0)
            .gift(reportDto.getTotalGift())
            .seminar(reportDto.getTotalSeminar())
            .lottery(reportDto.getTotalLottery())
            .vip_guest(reportDto.getTotalVip_guest())
            .stall(reportDto.getTotalStall())
            .build();
        reportRepository.save(report);
        return report;
    }
    @Override
    public Report update(ReportDto reportDto, Long id) throws CustomException {
        return reportRepository.findById(id).map(
            report -> {
                report.setGift(reportDto.getTotalGift());
                report.setHall(reportDto.getTotalHall());
                report.setLottery(reportDto.getTotalLottery());
                report.setQuestion(reportDto.getTotalQuestion());
                report.setManagers(reportDto.getTotalManagers());
                report.setRegisteredGuest(reportDto.getTotalRegisteredGuest());
                report.setSeminar(reportDto.getTotalSeminar());
                report.setStaff(reportDto.getTotalStaff());
                report.setVip_guest(reportDto.getTotalVip_guest());
                report.setVote(reportDto.getTotalVote());
                report.setVoteHallPavilion(reportDto.getTotalVoteHallPavilion());
                return reportRepository.save(report);
            }
        ).orElseThrow(() -> new CustomException("Report not found by given id"));
    }


    @Override
    public void removeById(Long id) throws CustomException {
        reportRepository.findById(id).orElseThrow(()->new CustomException("Report not found by given id"));
        reportRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAll(Long expoId) throws CustomException {
        checkValidExpoId(expoId);
        reportRepository.deleteAllByExpoId(expoId);
    }
    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
