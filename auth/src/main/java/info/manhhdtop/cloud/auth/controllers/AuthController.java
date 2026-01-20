package info.manhhdtop.cloud.auth.controllers;

import info.manhhdtop.cloud.auth.dtos.requests.ForgotPasswordRequest;
import info.manhhdtop.cloud.auth.dtos.requests.LoginRequest;
import info.manhhdtop.cloud.auth.dtos.requests.RegisterRequest;
import info.manhhdtop.cloud.auth.dtos.requests.ResetPasswordRequest;
import info.manhhdtop.cloud.auth.services.AuthService;
import info.manhhdtop.cloud.common.core.constants.ServiceConstant;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(ServiceConstant.Auth.BASE_URL)
@RestController
@Slf4j
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping(ServiceConstant.Auth.LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<?> response = ApiResponse.success(authService.login(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping(ServiceConstant.Auth.REGISTER)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        ApiResponse<?> response = ApiResponse.success(authService.register(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping(ServiceConstant.Auth.LOGOUT)
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping(ServiceConstant.Auth.REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken() {
        ApiResponse<?> response = ApiResponse.success(authService.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping(ServiceConstant.Auth.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping(ServiceConstant.Auth.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
