package info.manhhdtop.cloud.auth.controllers;

import info.manhhdtop.cloud.auth.services.UserService;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/users/{userId}/lock")
    public ResponseEntity<?> lockUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String reason) {
        userService.lockUser(userId, reason != null ? reason : "User locked by administrator");
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/users/{userId}/unlock")
    public ResponseEntity<?> unlockUser(@PathVariable Long userId) {
        userService.unlockUser(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> changeUserStatus(
            @PathVariable Long userId,
            @RequestParam @NotNull UserStatus status) {
        userService.changeUserStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}

