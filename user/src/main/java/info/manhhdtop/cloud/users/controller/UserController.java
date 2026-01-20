package info.manhhdtop.cloud.users.controller;

import info.manhhdtop.cloud.common.core.constants.ServiceConstant;
import info.manhhdtop.cloud.common.core.constants.ServiceConstant.User;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(ServiceConstant.User.BASE_URL)
@RestController
@Slf4j
@Validated
public class UserController {

    private final UserService service;

    @GetMapping(User.GET_ME)
    public ResponseEntity<ApiResponse<?>> getMe() {
        var result = service.getMe();
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
