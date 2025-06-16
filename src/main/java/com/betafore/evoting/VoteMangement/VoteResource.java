package com.betafore.evoting.VoteMangement;

import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.StallManagement.Stall;
import lombok.Data;

@Data
public class VoteResource {

    private final Hall hall;
    private final Stall stall;
    private final Question question;
    private final Long voteCount;

}
