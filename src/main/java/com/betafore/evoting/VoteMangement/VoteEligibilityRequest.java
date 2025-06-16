package com.betafore.evoting.VoteMangement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteEligibilityRequest {

    private Long hallId;
    private Long userId;

}
