package com.betafore.evoting.StallManagement;

import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.entities.BaseEntity;
import com.betafore.evoting.VoteMangement.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(
    name = "stalls"
)
public class Stall extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotBlank(message = "Stall name is mandatory")
    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String logo = null;

    @Column
    private String background = null;

    @JsonIgnore
    @OneToMany(mappedBy = "stall", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Vote> votes;

    @Column
    private String identity;
}
