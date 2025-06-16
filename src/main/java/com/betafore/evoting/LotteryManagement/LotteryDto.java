package com.betafore.evoting.LotteryManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotteryDto {

    @NotBlank(message = "Event name is mandatory")
    private String eventName;

    @NotNull(message = "Number of prize is mandatory")
    private Integer prizeCount;

}
