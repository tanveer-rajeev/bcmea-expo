package com.betafore.evoting.ExpoServiceManagement;

import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expo_service")
public class ExpoService extends BaseEntity {

    @NotBlank(message = "Service name is mandatory")
    private String name;

    @NotBlank(message = "Status is mandatory")
    private String status;

}
