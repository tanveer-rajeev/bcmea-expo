package com.betafore.evoting.PrizeMenagement;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrizeResponse {
    private Long id;
    private Long expoId;
    private String name;
    private String type;
    private String img;
}
