package com.betafore.evoting.PrizeMenagement;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PrizeDto {

    @NotBlank(message = "Prize name is mandatory")
    private String name;

    @NotBlank(message = "Prize type is mandatory")
    private String type;

    private MultipartFile img = null;
}
