package com.betafore.evoting.VoteMangement;

import com.betafore.evoting.HallManagement.HallProjection;
import com.betafore.evoting.QuestionMangement.QuestionProjection;
import com.betafore.evoting.StallManagement.StallProjection;

public interface VoteResult {

    Long getId();
    HallProjection getHall();
    StallProjection getStall();
    QuestionProjection getQuestion();
    Long getTotalVote();

}
