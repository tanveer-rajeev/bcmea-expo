package com.betafore.evoting.VoteMangement;

import com.betafore.evoting.UserManagement.UserProjection;

import java.time.LocalDateTime;

public interface VoteListBase {

    Long getId();
    UserProjection getUser();
    LocalDateTime getCreatedAt();

}
