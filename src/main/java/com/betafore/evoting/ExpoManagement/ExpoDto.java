package com.betafore.evoting.ExpoManagement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpoDto {
    @NotBlank(message = "Expo name is mandatory")
    private String expoName;

    @NotBlank(message = "Expo status is mandatory")
    private String status;

    @NotBlank(message = "Client name is mandatory")
    private String clientName;

    private Set<Long> expoServices = new HashSet<>();
}
