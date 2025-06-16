package com.betafore.evoting.VoteMangement;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VoteRequest {

    private Long hallId;
    private Long stallId;
    private Long userId;
    private Long questionId;

}
