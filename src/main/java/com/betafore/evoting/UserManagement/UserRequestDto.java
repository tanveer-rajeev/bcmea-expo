package com.betafore.evoting.UserManagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto implements Serializable {

    private Long id;

    private Long expoId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Phone number is mandatory.")
    @Pattern(regexp = "^([01]|\\+88)?\\d{11}",message = "phone number not valid")
    private String phoneNumber;
    
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email should match the pattern a-z @ a-z .com")
    private String email;

    @NotBlank(message = "Company is mandatory")
    private String company;

    private String country;

    private String address;

    private String profession;


}
