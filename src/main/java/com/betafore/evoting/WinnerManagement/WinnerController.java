package com.betafore.evoting.WinnerManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/winners")
public class WinnerController {

    private final WinnerService winnerService;

    @Operation(summary = "Get winner")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/by/{lotteryId}/{expoId}")
    public ResponseEntity<ApiResponse> getWinner(@PathVariable Long lotteryId,@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(winnerService.getWinner(expoId,lotteryId))
                .build());
    }
    @Operation(summary = "Get possible winner list")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/getPossibleWinnerList/{lotteryId}/{expoId}")
    public ResponseEntity<ApiResponse> getPossibleWinnerList(@PathVariable Long lotteryId,@PathVariable Long expoId) throws CustomException, IOException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(winnerService.getPossibleWinnerList(expoId,lotteryId))
                .build());
    }

    @Operation(summary = "Select winner")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_CREATE)
    @PostMapping("/acceptedWinner/{expoId}")
    public ResponseEntity<ApiResponse> saveWinner(@Valid @RequestBody WinnerDto winner,
                                                  @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message("Winner Selected")
                .data(winnerService.save(winner,expoId))
                .build());
    }

    @Operation(summary = "Update winner by id")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_UPDATE)
    @PutMapping("/{winnerId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long winnerId,@Valid @RequestBody WinnerDto winnerDto) throws CustomException {
        winnerService.saveAndFlush(winnerDto, winnerId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get all winner by lottery id")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/all/{lotteryId}/{expoId}")
    public ResponseEntity<ApiResponse> getAllWinner(@PathVariable Long lotteryId,
                                                    @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(winnerService.all(expoId,lotteryId))
                .build());
    }

    @Operation(summary = "Delete winner")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_DELETE)
    @DeleteMapping("/{winnerId}")
    public ResponseEntity<ApiResponse> deleteWinnerById(@PathVariable Long winnerId) throws CustomException {
        winnerService.removeById(winnerId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all winner")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllWinner(@PathVariable Long expoId) throws CustomException {
        winnerService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

}
