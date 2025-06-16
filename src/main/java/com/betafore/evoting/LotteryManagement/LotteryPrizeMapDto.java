package com.betafore.evoting.LotteryManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotteryPrizeMapDto {
    private List<Long> ids;
    private List<LotteryPrizeMap> lotteryPrizeMapSet;
}
