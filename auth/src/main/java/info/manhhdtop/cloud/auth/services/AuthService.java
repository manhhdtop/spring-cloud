package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.dtos.requests.ForgotPasswordRequest;
import info.manhhdtop.cloud.auth.dtos.requests.LoginRequest;
import info.manhhdtop.cloud.auth.dtos.requests.RegisterRequest;
import info.manhhdtop.cloud.auth.dtos.requests.ChangePasswordRequest;
import info.manhhdtop.cloud.auth.dtos.responses.LoginDto;
import info.manhhdtop.cloud.common.core.dtos.UserDto;

public interface AuthService {
    LoginDto login(LoginRequest request);

    UserDto register(RegisterRequest request);

    void logout();

    LoginDto refreshToken();

    void forgotPassword(ForgotPasswordRequest request);

    void changePassword(ChangePasswordRequest request);
}
