package com.betafore.evoting.WinnerManagement;

import com.betafore.evoting.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "winner")
public class Winner extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @NotNull(message = "User id is mandatory")
    private Long userId;

    @NotNull(message = "Lottery id is mandatory")
    private Long lotteryId;

    @NotNull(message = "Placement is mandatory")
    private Integer placement;

}
