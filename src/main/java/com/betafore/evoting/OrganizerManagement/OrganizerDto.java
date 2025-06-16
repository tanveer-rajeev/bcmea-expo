package com.betafore.evoting.OrganizerManagement;

//import com.betafore.evoting.security_config.RoleEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerDto {

    private Long id;
    private Long expoId;
    private Long roleId;
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email should match the pattern a-z @ a-z .com")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^([01]|\\+88)?\\d{11}", message = "phone number not valid")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    private String password;

    private String enabled;


}
