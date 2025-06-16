package com.betafore.evoting.SeminarManagement;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarDto {

    @NotBlank(message = "Seminar name is mandatory")
    private String name;
    @NotBlank(message = "Seminar type is mandatory")
    private String type;
    private MultipartFile img = null;
    private Integer duration;
    private Integer numOfGuest;
    private String time;
    private Integer capacity;
}
