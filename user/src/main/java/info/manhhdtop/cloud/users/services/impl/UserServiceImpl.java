package info.manhhdtop.cloud.users.services.impl;

import info.manhhdtop.cloud.common.core.dtos.RequestContext;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import info.manhhdtop.cloud.users.repositories.UserRepository;
import info.manhhdtop.cloud.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getMe() {
        Long userId = RequestContext.userId();
        var user = userRepository.findById(userId);
        return JsonUtil.convert(user, UserDto.class);
    }
}
