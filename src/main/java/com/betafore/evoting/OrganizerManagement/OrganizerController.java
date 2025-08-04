package com.betafore.evoting.OrganizerManagement;

import com.betafore.evoting.Exception.ApiResponse;
import com.betafore.evoting.Exception.CustomException;
import com.betafore.evoting.security_config.CustomHasAuthority;
import com.betafore.evoting.RolePermissionManagement.PermissionEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.betafore.evoting.Common.ResponseMessageConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/organizers")
public class OrganizerController {

    private final OrganizerService organizerService;

    @Operation(summary = "Get logged in organizer info")
    @GetMapping("/loggedInOrg")
    public ResponseEntity<ApiResponse> loggedInOrganizer() throws CustomException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String principal = (String) authentication.getPrincipal();
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(organizerService.findByEmail(principal)).build());
    }

    @Operation(summary = "Create organizer")
    @PostMapping("/signup/{expoId}")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_CREATE)
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody OrganizerDto request,
                                                @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(organizerService.save(request,expoId)).build());
    }

    @Operation(summary = "Login organizer")
    @PostMapping("/login/{expoId}")
    public ResponseEntity<ApiResponse> signIn(@Valid @RequestBody AuthenticationRequest request,@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message("Login Successfully")
                .data(organizerService.authenticate(request,expoId)).build());
    }

    @Operation(summary = "Login organizer by phone")
    @PostMapping("/login/byPhone/{phoneNumber}/{expoId}")
    public ResponseEntity<ApiResponse> signInByPhone(@PathVariable String phoneNumber,@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message("Login Successfully")
                .data(organizerService.authenticateByPhoneNumber(phoneNumber,expoId)).build());
    }

    @Operation(summary = "Get organizer")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_READ)
    @GetMapping("/{organizerId}")
    public ResponseEntity<ApiResponse> getOrganizerById(@PathVariable Long organizerId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(organizerService.findById(organizerId)).build());
    }

    @Operation(summary = "Get all organizer")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_READ)
    @GetMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllOrganizer(@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(organizerService.all(expoId)).build());
    }

    @Operation(summary = "Get all organizer")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_READ)
    @GetMapping("/all/role/{organizerId}")
    public ResponseEntity<ApiResponse> getAllRoleByOrganizerId(@PathVariable Long organizerId) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(organizerService.getAllRoleByOrganizerId(organizerId)).build());
    }

    @Operation(summary = "Update organizer")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_UPDATE)
    @PutMapping("/{organizerId}")
    public ResponseEntity<ApiResponse> updateOrganizer(@PathVariable Long organizerId,@RequestBody OrganizerDto organizer) throws CustomException {
        organizerService.update(organizerId,organizer);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Delete organizer")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_DELETE)
    @DeleteMapping("/{organizerId}")
    public ResponseEntity<ApiResponse> deleteOrganizerById(@PathVariable Long organizerId) throws CustomException {
        organizerService.removeById(organizerId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all organizer by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.ORGANIZER_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllOrganizer(@PathVariable Long expoId) throws CustomException {
        organizerService.removeAll(expoId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Reset password")
    @PutMapping("/resetPassword/{expoId}")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto, @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .data(organizerService.resetPassword(resetPasswordDto, expoId))
                .build());
    }
}
