package com.betafore.evoting.EmploymentManagement;

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
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Create employee")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_CREATE)
    @PostMapping("/{participantId}/{expoId}")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody Employee employee,
                                            @PathVariable Long participantId,
                                            @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(employeeService.save(employee, participantId, expoId)).build());
    }

    @Operation(summary = "Update employee")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_UPDATE)
    @PutMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long employeeId, @RequestBody Employee employee) throws CustomException {
        employeeService.updateEmployee(employeeId, employee);

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get employee")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_READ)
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable Long employeeId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(employeeService.findById(employeeId))
                .build());
    }

    @Operation(summary = "Get all employee by participant id and expo id")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_READ)
    @GetMapping("/all/byParticipant/{participantId}")
    public ResponseEntity<ApiResponse> getAllEmployeeByParticipant(@PathVariable Long participantId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(employeeService.allEmployeeByParticipant(participantId))
                .build());
    }

    @Operation(summary = "Get all employee by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllEmployee(@PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(employeeService.allByExpoId(expoId))
                .build());
    }

    @Operation(summary = "Delete employee")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_DELETE)
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> deleteEmployeeById(@PathVariable Long employeeId) throws CustomException {
        employeeService.removeById(employeeId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all employee")
    @CustomHasAuthority(authorities = PermissionEnum.EMPLOYEE_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllEmployee(@PathVariable Long expoId) throws CustomException {
        employeeService.removeAll(expoId);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }
}
