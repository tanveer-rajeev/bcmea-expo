package com.betafore.evoting.ParticipantManagement;

import com.betafore.evoting.EmploymentManagement.Employee;
import com.betafore.evoting.VIP_GuestManagement.VIP_Guest;
import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participant")
public class Participant extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String type;
    private Integer vipGuestLimit;
    private Integer activeGuestCount;
    private Long organizerId;

    @JsonIgnore
    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY)
    private List<VIP_Guest> vipGuestList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY)
    private List<Employee> employeeList = new ArrayList<>();

}
