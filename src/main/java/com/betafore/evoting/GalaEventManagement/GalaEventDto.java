package com.betafore.evoting.GalaEventManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GalaEventDto {

    @NotBlank(message = "Gala event name is mandatory")
    private String name;
    private String type;
    private MultipartFile img = null;
    private Integer duration;

    private Integer numOfGuest;
    private String time;
    @NotNull(message = "Gala event capacity is mandatory")
    private Integer capacity;
}
