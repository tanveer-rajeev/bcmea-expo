package com.betafore.evoting.ExpoServiceManagement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpoServiceDto {

    private List<ExpoService> expoServices;
}
