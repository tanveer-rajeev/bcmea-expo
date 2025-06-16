package com.betafore.evoting.UserManagement;

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
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register user")
    @PostMapping("/register/{expoId}")
    public ResponseEntity<ApiResponse> registration(@Valid @RequestBody UserRequestDto user,
                                                    @PathVariable Long expoId) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(userService.registration(user, expoId)).build());
    }

    @Operation(summary = "Create  user")
    @PostMapping("/createUser/{expoId}")
    @CustomHasAuthority(authorities = PermissionEnum.USER_CREATE)
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody UserRequestDto user,
                                              @PathVariable Long expoId, @RequestParam(required = false) boolean sendEmail,
                                              @RequestParam(required = false) boolean sendSms) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(CREATED)
                .data(userService.createUser(user, expoId, sendEmail, sendSms)).build());
    }

    @Operation(summary = "User verification and confirmation")
    @CustomHasAuthority(authorities = PermissionEnum.USER_UPDATE)
    @PutMapping(path = "/userConfirmation/{userId}")
    public ResponseEntity<ApiResponse> userConfirmationAndUpdate(@PathVariable Long userId, @RequestBody UserConfirmationDto userConfirmationDto
    ) throws CustomException {
        userService.updateByStaff(userId, userConfirmationDto);
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .build());
    }

    @Operation(summary = "Get a user by phone number")
    @CustomHasAuthority(authorities = PermissionEnum.USER_READ)
    @GetMapping("/byPhone/{phoneNumber}")
    public ResponseEntity<ApiResponse> getUserByPhoneNumber(@PathVariable String phoneNumber) throws CustomException {
        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(userService.findUserByPhoneNumber(phoneNumber))
                .build());
    }

    @Operation(summary = "Get a user")
    @CustomHasAuthority(authorities = PermissionEnum.USER_READ)
    @GetMapping("/byId/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(userService.findById(id)).build());
    }

    @Operation(summary = "User entry api")
    @CustomHasAuthority(authorities = PermissionEnum.USER_READ)
    @GetMapping("/scan/{id}")
    public ResponseEntity<ApiResponse> userScan(@PathVariable String id) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(userService.userScan(id)).build());
    }

    @Operation(summary = "Get all user by expo id")
    @CustomHasAuthority(authorities = PermissionEnum.USER_READ)
    @GetMapping(value = "/all/{expoId}")
    public ResponseEntity<ApiResponse> getAllUserByIds(@PathVariable Long expoId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(userService.all(expoId)).build());
    }

    @Operation(summary = "Update user")
    @CustomHasAuthority(authorities = PermissionEnum.USER_UPDATE)
    @PutMapping("/byUser/{userId}")
    public ResponseEntity<ApiResponse> updateByUser(@RequestBody UserRequestDto user, @PathVariable Long userId) throws CustomException {

        return ResponseEntity.ok()
            .body(ApiResponse.builder()
                .success(true)
                .message(UPDATED)
                .data(userService.updateByUser(user, userId)).build());
    }


    @Operation(summary = "Get user's seminar list")
    @CustomHasAuthority(authorities = PermissionEnum.USER_READ)
    @GetMapping("/seminarList/{id}")
    public ResponseEntity<ApiResponse> getSeminarList(@PathVariable Long id) throws CustomException {

        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(FETCHED)
                .data(userService.getSeminarListFromUser(id))
                .build());
    }

    @Operation(summary = "Delete user")
    @CustomHasAuthority(authorities = PermissionEnum.USER_DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long id) throws CustomException {
        userService.removeById(id);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }

    @Operation(summary = "Delete all user")
    @CustomHasAuthority(authorities = PermissionEnum.USER_DELETE)
    @DeleteMapping("/all/{expoId}")
    public ResponseEntity<ApiResponse> deleteAllUser(@PathVariable Long expoId) throws CustomException {
        userService.removeAll(expoId);
        return ResponseEntity.status(200)
            .body(ApiResponse.builder()
                .success(true)
                .message(DELETED)
                .build());
    }


}
