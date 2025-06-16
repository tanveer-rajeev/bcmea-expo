package com.betafore.evoting.QuestionMangement;

import com.betafore.evoting.StallManagement.Stall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private Set<Stall> options;
}
