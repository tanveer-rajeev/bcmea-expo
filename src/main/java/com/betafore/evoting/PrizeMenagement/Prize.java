package com.betafore.evoting.PrizeMenagement;

import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "prize")
public class Prize extends BaseEntity {
    @Column(name = "expoId")
    private Long expoId;
    @NotBlank(message = "Prize name is mandatory")
    private String name;
    @NotBlank(message = "Prize type is mandatory")
    private String type;
    private String img = null;
}
