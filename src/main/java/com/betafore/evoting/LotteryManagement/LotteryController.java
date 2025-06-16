package com.betafore.evoting.LotteryManagement;

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
@RequestMapping("/api/v1/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    @Operation(summary = "Start lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_CREATE)
    @PostMapping("/start/{lotteryId}")
    public ResponseEntity<ApiResponse> startLottery(@PathVariable Long lotteryId) throws CustomException {
        lotteryService.startLottery(lotteryId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(LOTTERY_STARTED)
                .build());
    }

    @Operation(summary = "End lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_UPDATE)
    @PostMapping("/end/{lotteryId}")
    public ResponseEntity<ApiResponse> endLottery(@PathVariable Long lotteryId) throws CustomException {
        lotteryService.endLottery(lotteryId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(LOTTERY_ENDED)
                .build());
    }


    @Operation(summary = "Create lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody LotteryDto lotteryDto,
                                            @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(lotteryService.save(lotteryDto, expoId))
                .build());
    }

    @Operation(summary = "Add prize into lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_CREATE)
    @PostMapping("/lotteryPrizes")
    public ResponseEntity<ApiResponse> addPrizeIntoLottery(@Valid @RequestBody LotteryPrizeMap lotteryPrizeMap) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(lotteryService.addPrizeIntoLottery(lotteryPrizeMap))
                .build());
    }

    @Operation(summary = "Add prizes into lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_CREATE)
    @PostMapping("/lotteryPrizes/all")
    public ResponseEntity<ApiResponse> addManyPrizeIntoLottery( @RequestBody LotteryPrizeMapDto lotteryPrizeMapDto) throws CustomException {
        lotteryService.addAllPrizeIntoLottery(lotteryPrizeMapDto);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .build());
    }

    @Operation(summary = "Update lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_UPDATE)
    @PutMapping("/{lotteryId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long lotteryId, @Valid @RequestBody LotteryDto lotteryDto) throws CustomException {
        lotteryService.saveAndFlush(lotteryDto, lotteryId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Update lottery prize map")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_UPDATE)
    @PutMapping("/lotteryPrizes/{id}")
    public ResponseEntity<ApiResponse> updateLotteryPrizes(@PathVariable Long id, @RequestBody LotteryPrizeMap lotteryDto) throws CustomException {
        lotteryService.updateLotteryPrizeMap(id, lotteryDto);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Update many lottery prize map")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_UPDATE)
    @PutMapping("/lotteryPrizes/all")
    public ResponseEntity<ApiResponse> updateAllLotteryPrizes(@RequestBody LotteryPrizeMapDto lotteryPrizeMapDto) throws CustomException {
        lotteryService.updateAllLotteryPrizeMap(lotteryPrizeMapDto);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Remove a prize from lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_DELETE)
    @DeleteMapping("/lotteryPrizes/{id}")
    public ResponseEntity<ApiResponse> removePrizeFromLottery(@PathVariable Long id) throws CustomException {
        lotteryService.removePrizeFromLottery(id);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Get lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/{lotteryId}")
    public ResponseEntity<ApiResponse> getLotteryById(@PathVariable Long lotteryId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(lotteryService.findById(lotteryId))
                .build());
    }

    @Operation(summary = "Get lottery's prize by position")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/lotteryPrizes/{position}/{lotteryId}")
    public ResponseEntity<ApiResponse> getLotteryById(@PathVariable Integer position, @PathVariable Long lotteryId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(lotteryService.findLotteryPrizeByPosition(lotteryId, position))
                .build());
    }

    @Operation(summary = "Get lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllLottery(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(lotteryService.all(expoId))
                .build());
    }

    @Operation(summary = "Get lottery prize map")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/lotteryPrizeMap/all/{lotteryId}")
    public ResponseEntity<ApiResponse> getAllLotteryPrizeMap(@PathVariable Long lotteryId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(lotteryService.getAllLotteryPrizeMapByLotteryId(lotteryId))
                .build());
    }

    @Operation(summary = "Get all lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_READ)
    @GetMapping("/allRunning/{expoId}")
    public ResponseEntity<ApiResponse> getAllRunningLottery(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(lotteryService.getAllRunningLottery(expoId))
                .build());
    }

    @Operation(summary = "Delete lottery")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_DELETE)
    @DeleteMapping("/{lotteryId}")
    public ResponseEntity<ApiResponse> deleteLotteryById(@PathVariable Long lotteryId) throws CustomException {
        lotteryService.removeById(lotteryId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all lottery by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.LOTTERY_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllLottery(@PathVariable Long expoId) throws CustomException {
        lotteryService.removeAll(expoId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

}
