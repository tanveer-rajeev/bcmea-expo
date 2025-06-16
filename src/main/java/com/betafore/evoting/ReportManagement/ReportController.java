package com.betafore.evoting.ReportManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get report by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.REPORT_READ)
    @GetMapping("/{expoId}")
    public ResponseEntity<ApiResponse> getReport(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(reportService.report(expoId))
                .build());
    }

    @Operation(summary = "Create report by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.REPORT_CREATE)
    @PostMapping("/{expoId}")
    public ResponseEntity<ApiResponse> save(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(reportService.save(expoId))
                .build());
    }

    @Operation(summary = "Update report")
    @CustomHasAuthority(authorities = PermissionEnum.REPORT_UPDATE)
    @PutMapping("/{reportId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long reportId, @RequestBody ReportDto reportDto) throws CustomException {
        reportService.update(reportDto, reportId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }


    @Operation(summary = "Delete report")
    @CustomHasAuthority(authorities = PermissionEnum.REPORT_DELETE)
    @DeleteMapping("/{reportId}")
    public ResponseEntity<ApiResponse> deleteReportById(@PathVariable Long reportId) throws CustomException {
        reportService.removeById(reportId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all report by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.REPORT_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllReport(@PathVariable Long expoId) throws CustomException {
        reportService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }
}
