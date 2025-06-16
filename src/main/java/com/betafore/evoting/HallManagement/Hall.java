package com.betafore.evoting.HallManagement;

import com.betafore.evoting.entities.BaseEntity;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.StallManagement.Stall;
import com.betafore.evoting.VoteMangement.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(
    name = "halls"
)
public class  Hall extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @Column
    private String name;

    @Column
    private String slug;

    @JsonIgnore
    @OneToMany(mappedBy = "hall", fetch = FetchType.LAZY)
    private List<Stall> stalls = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hall", fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hall",fetch = FetchType.LAZY)
    private Set<Question> questions;

    public void addQuestion(Question question) {
        if (questions.isEmpty()) {
            questions = new HashSet<>();
        }
        questions.add(question);
    }
    public void removeQuestion(Question question) {
        if (questions.isEmpty()) {
            return;
        }
        questions.remove(question);
    }
}
