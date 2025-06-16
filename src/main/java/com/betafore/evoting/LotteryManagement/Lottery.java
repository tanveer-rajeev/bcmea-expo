package com.betafore.evoting.LotteryManagement;

import com.betafore.evoting.PrizeMenagement.Prize;
import com.betafore.evoting.SeminarManagement.Seminar;
import com.betafore.evoting.WinnerManagement.Winner;
import com.betafore.evoting.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lottery")
public class Lottery extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @NotBlank(message = "Event name is mandatory")
    private String eventName;

    @NotNull(message = "Number of prize is mandatory")
    private Integer prizeCount;

    private String state;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Winner> winnerSet;

    public void addWinner(Winner winner) {
        if (winnerSet == null) {
            winnerSet = new HashSet<>();
        }
        winnerSet.add(winner);
    }

    public void removeWinner(Winner winner) {
        if (winnerSet == null) {
            return;
        }
        winnerSet.remove(winner);
    }

}
