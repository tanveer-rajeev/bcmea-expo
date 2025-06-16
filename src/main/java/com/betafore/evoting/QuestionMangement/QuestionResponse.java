package com.betafore.evoting.QuestionMangement;

import com.betafore.evoting.StallManagement.StallResource;
import com.betafore.evoting.StallManagement.StallResourceMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private Long id;
    private Long expoId;
    private String title;
    private String question;
    private List<StallResource> stallResourceMapper;
}
