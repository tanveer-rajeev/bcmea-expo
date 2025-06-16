package com.betafore.evoting.WinnerManagement;

import com.betafore.evoting.LotteryManagement.Lottery;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinnerDto {
    @NotNull(message = "User id is mandatory")
    private Long userId;

    @NotNull(message = "Placement is mandatory")
    private Integer placement;

    @NotNull(message = "lottery id is mandatory")
    private Long lotteryId;

}
