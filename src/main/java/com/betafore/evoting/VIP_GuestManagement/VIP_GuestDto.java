package com.betafore.evoting.VIP_GuestManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VIP_GuestDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^([01]|\\+88)?\\d{11}",message = "phone number not valid")
    private String phoneNumber;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email should match the pattern a-z @ a-z .com")
    private String email;

    private String company;
    private String profession;
    private String country;
    private MultipartFile img = null;
}
