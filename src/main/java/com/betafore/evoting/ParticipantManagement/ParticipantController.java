package com.betafore.evoting.ParticipantManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Operation(summary = "Create participant by expo id")
    @PostMapping("/{organizerId}/{expoId}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_CREATE)
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody Participant participant,
                                            @PathVariable Long organizerId,
                                            @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(participantService.save(participant, organizerId, expoId))
                .build());
    }

    @Operation(summary = "Get participant")
    @GetMapping("/{id}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_READ)
    public ResponseEntity<ApiResponse> getParticipantById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(participantService.findById(id))
                .build());
    }

    @Operation(summary = "Get all participant by expo id")
    @GetMapping("/all/{expoId}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_READ)
    public ResponseEntity<ApiResponse> getAllParticipantByIds(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(participantService.all(expoId))
                .build());
    }

    @Operation(summary = "Update participant")
    @PutMapping("/{id}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_UPDATE)
    public ResponseEntity<ApiResponse> updateParticipant(@RequestBody Participant participant, @PathVariable Long id) throws CustomException {
        participantService.updateParticipant(participant, id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }


    @Operation(summary = "Delete participant")
    @DeleteMapping("/{id}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_DELETE)
    public ResponseEntity<ApiResponse> deleteParticipantById(@PathVariable Long id) throws CustomException {
        participantService.removeById(id);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all participant by expo id")
    @DeleteMapping("/all/{expoId}")
    @CustomHasAuthority(authorities = PermissionEnum.PARTICIPANT_DELETE)
    public ResponseEntity<ApiResponse> deleteAllParticipant(@PathVariable Long expoId) throws CustomException {
        participantService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }
}
