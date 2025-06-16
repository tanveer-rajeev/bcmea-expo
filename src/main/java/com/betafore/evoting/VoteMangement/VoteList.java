package com.betafore.evoting.VoteMangement;

import com.betafore.evoting.HallManagement.HallProjection;
import com.betafore.evoting.QuestionMangement.QuestionProjection;
import com.betafore.evoting.StallManagement.StallProjection;

public interface VoteList extends VoteListBase {

    HallProjection getHall();
    StallProjection getStall();
    QuestionProjection getQuestion();

}
