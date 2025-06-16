package com.betafore.evoting.EmploymentManagement;

import com.betafore.evoting.ParticipantManagement.Participant;
import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "user_name")
    private String name;

    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phone_number")
    @Pattern(regexp = "^([01]|\\+88)?\\d{11}",message = "phone number not valid")
    private String phoneNumber;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email should match the pattern a-z @ a-z .com")
    private String email;

    @Column(name = "address")
    private String address;

    @NotBlank(message = "Company is mandatory")
    @Column(name = "company")
    private String company;

    @Column(name = "profession")
    private String designation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    public Participant participant;

}
