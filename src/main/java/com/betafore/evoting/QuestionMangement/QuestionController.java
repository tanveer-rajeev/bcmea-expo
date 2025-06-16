package com.betafore.evoting.QuestionMangement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.HallManagement.HallRepository;
import com.betafore.evoting.StallManagement.Stall;
import com.betafore.evoting.StallManagement.StallResource;
import com.betafore.evoting.StallManagement.StallResourceMapper;
import com.betafore.evoting.StallManagement.StallService;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.EmptyResultDataAccessException;

import com.betafore.evoting.response.ApiResponse;
import com.betafore.evoting.response.MessageResponse;
import com.betafore.evoting.response.ResultResponse;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService service;
    private final StallService stallService;
    private final ExpoRepository expoRepository;
    private final HallRepository hallRepository;

    @Operation(summary = "Remove stall from a question by given stall id")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_UPDATE)
    @PutMapping("/removeStall/{questionId}/{stallId}")
    @Transactional
    public ResponseEntity<ApiResponse> deleteStallFromQuestion(@PathVariable Long questionId,
                                                               @PathVariable Long stallId) throws CustomException {
        Question question = service.getQuestionById(questionId);

        Stall stall = stallService.findById(stallId);
        question.removeStall(stall);

        ApiResponse response = new ResultResponse(question);
        response.setStatus("success");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Get all question")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ResultResponse> list(@PathVariable Long expoId) throws CustomException {
        List<Question> questions = service.getAll(expoId);
        ResultResponse response = new ResultResponse(questions);
        response.setStatus("success");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Create question")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody QuestionRequest request,
                                            @PathVariable Long expoId) throws CustomException {
        expoRepository.findById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));

        Optional<Question> optionalQuestion = service.getByQuestionAndExpoId(request.getQuestion(), expoId);
        if (optionalQuestion.isPresent())
            throw new CustomException("Question already present in server");

        Set<Stall> listOfStall = new HashSet<>();

        for (Long stallId : request.getStalls()) {
            Stall stall = stallService.findById(stallId);
            if (!stall.getExpoId().equals(expoId)) {
                throw new CustomException("Stall not present in the expo");
            }
            listOfStall.add(stall);
        }
        Hall hall = hallRepository.findById(request.getHallId()).orElseThrow(() -> new CustomException("Hall not found by given id"));

        Question question = Question.builder()
            .expoId(expoId)
            .title(request.getTitle()).question(request.getQuestion())
            .stalls(listOfStall)
            .hall(hall)
            .build();
        service.save(question);
        return ResponseEntity.ok().body(
            new MessageResponse("Question has been created successfully.")
        );
    }

    @Operation(summary = "Get question")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_READ)
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> show(@PathVariable Long id) {
        Optional<Question> optional = service.getById(id);

        if (optional.isEmpty()) {
            ApiResponse response = new MessageResponse("Sorry, we couldn't find any matching question.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Question question = optional.get();
        List<StallResource> stallResources = new StallResourceMapper().collection(question.getStalls().stream().toList());
        QuestionResponse questionResponse = QuestionResponse.builder().id(question.getId())
            .expoId(question.getExpoId()).title(question.getTitle())
            .question(question.getQuestion()).stallResourceMapper(stallResources).build();
        ApiResponse response = new ResultResponse(questionResponse);
        response.setStatus("success");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update question")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_UPDATE)
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) throws CustomException {
        Optional<Question> optional = service.getById(id);

        if (optional.isEmpty()) {
            ApiResponse response = new MessageResponse("Sorry, we couldn't find any matching question.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Question question = optional.get();
        Set<Stall> stalls = new HashSet<>();

        if (!request.getStalls().isEmpty()) {
            question.getStalls().clear();
            for (Long stallId : request.getStalls()) {
                Stall stall = stallService.getById(stallId).get();
                if (!stall.getExpoId().equals(question.getExpoId())) {
                    throw new CustomException("Stall not present in the expo");
                }
                stalls.add(stall);
            }
            question.addStallList(stalls);
        }
        Hall hall = hallRepository.findById(request.getHallId()).orElseThrow(() -> new CustomException("Hall not found by given id"));

        if (request.getHallId() != null) {
            question.setHall(hall);
        }

        if (request.getTitle() != null) {
            question.setTitle(request.getTitle());
        }

        if (!question.getQuestion().equals(request.getQuestion()) && request.getQuestion() != null) {
            Optional<Question> optionalQuestion = service.getByQuestionAndExpoId(request.getQuestion(), question.getExpoId());
            if (optionalQuestion.isPresent())
                throw new CustomException("Question already present in server");

            question.setQuestion(request.getQuestion());
        }
        ApiResponse response = new MessageResponse("Question has been updated successfully.");
        response.setStatus("success");
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Delete question")
    @CustomHasAuthority(authorities = PermissionEnum.QUESTION_DELETE)
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) throws CustomException {
        try {
            service.deleteById(id);
            ApiResponse response = new MessageResponse("Question has been updated successfully.");
            response.setStatus("success");
            return ResponseEntity.ok().body(response);
        } catch (EmptyResultDataAccessException e) {
            ApiResponse response = new MessageResponse("Sorry, we couldn't find any matching question.");
            response.setStatus("error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
