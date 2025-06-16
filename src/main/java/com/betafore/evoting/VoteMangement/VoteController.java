package com.betafore.evoting.VoteMangement;

import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.ExpoManagement.ExpoRepository;
import com.betafore.evoting.HallManagement.Hall;
import com.betafore.evoting.HallManagement.HallService;
import com.betafore.evoting.QuestionMangement.Question;
import com.betafore.evoting.QuestionMangement.QuestionService;
import com.betafore.evoting.StallManagement.Stall;
import com.betafore.evoting.StallManagement.StallService;
import com.betafore.evoting.UserManagement.User;
import com.betafore.evoting.UserManagement.UserService;
import com.betafore.evoting.response.ApiResponse;
import com.betafore.evoting.response.MessageResponse;
import com.betafore.evoting.response.ResultResponse;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {

    private final VoteService service;
    private final HallService hallService;
    private final StallService stallService;
    private final QuestionService questionService;
    private final UserService userService;
    private final ExpoRepository expoRepository;


    private List<? extends VoteListBase> filterGetAll(Long expoId, String hallSlug, String stallSlug, Long questionId) {
        List<? extends VoteListBase> votes;

        if (hallSlug != null && !hallSlug.isEmpty() && stallSlug != null && !stallSlug.isEmpty() && questionId != null) {
            votes = service.filterResultByHallStallQuestion(hallSlug, stallSlug, questionId);
        } else if (hallSlug != null && !hallSlug.isEmpty() && stallSlug != null && !stallSlug.isEmpty()) {
            votes = service.filterResultByHallStall(hallSlug, stallSlug);
        } else if (hallSlug != null && !hallSlug.isEmpty() && questionId != null) {
            votes = service.filterResultByHallQuestion(hallSlug, questionId);
        } else if (stallSlug != null && !stallSlug.isEmpty() && questionId != null) {
            votes = service.filterResultByStallQuestion(stallSlug, questionId);
        } else if (hallSlug != null && !hallSlug.isEmpty()) {
            votes = service.filterResultByHall(hallSlug);
        } else if (stallSlug != null && !stallSlug.isEmpty()) {
            votes = service.filterResultByStall(stallSlug);
        } else if (questionId != null) {
            votes = service.filterResultByQuestion(questionId);
        } else {
            votes = service.getAll(expoId);
        }
        return votes;
    }

    @Operation(summary = "Get all votes")
    @CustomHasAuthority(authorities = PermissionEnum.VOTE_DELETE)
    @GetMapping("/{expoId}")
    public ResponseEntity<ResultResponse> getAll(
        @PathVariable Long expoId,
        @RequestParam(required = false) String hall,
        @RequestParam(required = false) String stall,
        @RequestParam(required = false) Long question
    ) throws CustomException {
        checkValidExpoId(expoId);
        List<? extends VoteListBase> votes = filterGetAll(expoId, hall, stall, question);
        ResultResponse response = new ResultResponse(votes);
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    private List<VoteResult> filterResult(Long expoId, String hallSlug, String stallSlug) {
        List<VoteResult> votes;

        if (hallSlug != null && stallSlug != null) {
            votes = service.resultOfAHallAndStall(hallSlug, stallSlug);
        } else if (hallSlug != null) {
            votes = service.resultOfAHall(hallSlug);
        } else if (stallSlug != null) {
            votes = service.resultOfAStall(stallSlug);
        } else {
            votes = service.voteCalculation(expoId);
        }
        return votes;
    }

    @Operation(summary = "Vote results")
    @CustomHasAuthority(authorities = PermissionEnum.VOTE_DELETE)
    @GetMapping("/results/{expoId}")
    public ResponseEntity<ResultResponse> results(
        @PathVariable Long expoId,
        @RequestParam(required = false) String hall,
        @RequestParam(required = false) String stall
    ) throws CustomException {
        checkValidExpoId(expoId);

        List<VoteResult> votes = filterResult(expoId, hall, stall);
        ResultResponse response = new ResultResponse(votes);
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @CustomHasAuthority(authorities = PermissionEnum.VIP_READ)
    @Operation(summary = "Vote submit")
    @PostMapping("/submit/{expoId}")
    public ResponseEntity<MessageResponse> submit(@RequestBody List<VoteRequest> request,
                                                  @PathVariable Long expoId) throws CustomException {
        List<Vote> votes = new ArrayList<>();
        checkValidExpoId(expoId);

        for (VoteRequest voteRequest : request) {
            User user = userService.findById(voteRequest.getUserId());
            if (!user.isAttendExpo()) {
                throw new CustomException("User not verified");
            }
            Optional<Hall> optionalHall = hallService.getById(voteRequest.getHallId());
            Optional<Stall> optionalStall = stallService.getById(voteRequest.getStallId());
            Optional<Question> optionalQuestion = questionService.getById(voteRequest.getQuestionId());

            if (optionalHall.isEmpty()) {
                MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching hall.");
                response.setStatus("error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (optionalStall.isEmpty()) {
                MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching stall.");
                response.setStatus("error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (optionalQuestion.isEmpty()) {
                MessageResponse response = new MessageResponse("Sorry, we couldn't find any matching question.");
                response.setStatus("error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            votes.add(
                new Vote(
                    expoId,
                    optionalHall.get(),
                    optionalStall.get(),
                    optionalQuestion.get(),
                    user
                )
            );
        }

        service.submit(votes);
        MessageResponse response = new MessageResponse("A vote has been submitted successfully.");
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @CustomHasAuthority(authorities = PermissionEnum.VIP_READ)
    @Operation(summary = "Vote eligibility")
    @PostMapping("/eligibility")
    public ResponseEntity<? extends ApiResponse> eligibility(@RequestBody VoteEligibilityRequest request) throws CustomException {
        User user = userService.findById(request.getUserId());
        if (!user.isAttendExpo()) throw new CustomException("User verification required");
        boolean result = service.eligibility(request.getHallId(), request.getUserId());
        return ResponseEntity.ok(
            new ResultResponse(result)
        );
    }

    @CustomHasAuthority(authorities = PermissionEnum.VOTE_DELETE)
    @GetMapping("/analytics/{expoId}")
    public ResponseEntity<ResultResponse> analytics(@RequestParam(required = false) String hall,
                                                    @PathVariable Long expoId) throws CustomException {
        List<VoteList> analytics;
        checkValidExpoId(expoId);
        if (hall != null && !hall.isEmpty()) {
            analytics = service.filterAnalyticsByHall(hall, expoId);
        } else {
            analytics = service.analytics(expoId);
        }
        return ResponseEntity.ok(
            new ResultResponse(analytics)
        );
    }

    private void checkValidExpoId(Long expoId) throws CustomException {
        expoRepository.findEnableExpoById(expoId).orElseThrow(() -> new CustomException("Expo not found by given id: " + expoId));
    }
}
