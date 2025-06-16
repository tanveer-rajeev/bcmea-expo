package com.betafore.evoting.GiftManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftDto {

    @NotBlank(message = "Gift name is mandatory")
    private String name;

    @NotBlank(message = "Gift type is mandatory")
    private String type;

    private String description;

    @NotNull(message = "Gift eligibility is mandatory")
    private Integer eligibility;

    private MultipartFile img = null;

    @NotNull(message = "Gift eligibility for all status is mandatory")
    private String isEligibleForAll ;

}
