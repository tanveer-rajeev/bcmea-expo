package com.betafore.evoting.StallManagement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StallRequest {

    private Long hallId;
    @NotBlank(message = "Stall name is mandatory")
    private String name;
    private MultipartFile logo = null;
    private MultipartFile background = null;
    private String identity;
}
