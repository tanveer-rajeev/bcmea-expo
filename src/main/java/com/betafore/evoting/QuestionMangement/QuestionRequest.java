package com.betafore.evoting.QuestionMangement;

import com.betafore.evoting.StallManagement.Stall;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class QuestionRequest {

    @NotBlank(message = "Question is mandatory")
    private String title;

    @NotBlank(message = "Question is mandatory")
    private String question;

    private Set<Long> stalls = new HashSet<>();

    private Long hallId;
}
