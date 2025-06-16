package com.betafore.evoting.ReportManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private Long expoId;
    private Integer totalRegisteredGuest;
    private Integer totalHall;
    private Integer totalStall;
    private Integer totalQuestion;
    private Integer totalManagers;
    private Integer totalStaff;
    private Integer totalVote;
    private Integer totalVoteHallPavilion;
    private Integer totalGift;
    private Integer totalSeminar;
    private Integer totalLottery;
    private Integer totalVip_guest;
}
