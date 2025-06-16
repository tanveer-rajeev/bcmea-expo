package com.betafore.evoting.QuestionMangement;

import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.entities.BaseEntity;
import com.betafore.evoting.StallManagement.Stall;
import com.betafore.evoting.VoteMangement.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Builder
@Table(name = "questions")
public class Question extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column
    private String title;

    @NotBlank(message = "Question is mandatory")
    @Column
    private String question;

    @JsonIgnore 
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Vote> votes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "questions_stalls",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "stall_id"))
    private Set<Stall> stalls;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    public void addStallList(Set<Stall> stall) {
        if (stalls.isEmpty()) {
            stalls = new HashSet<>();
        }
        stalls.addAll(stall);
    }
    public void removeStall(Stall stall) {
        if (stalls.isEmpty()) {
            return;
        }
        stalls.remove(stall);
    }
}
