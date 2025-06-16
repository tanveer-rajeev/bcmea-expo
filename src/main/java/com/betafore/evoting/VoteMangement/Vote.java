package com.betafore.evoting.VoteMangement;
import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.StallManagement.Stall;
import com.betafore.evoting.UserManagement.User;
import com.betafore.evoting.entities.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(
    name = "votes"
)
public class Vote extends BaseEntity {

    @Column(name = "expoId")
    private Long expoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stall_id")
    private Stall stall;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

}
