package info.manhhdtop.cloud.auth.feignclients;

import info.manhhdtop.cloud.common.core.constants.ServiceConstant;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("users")
public interface UserClient {
    @GetMapping(ServiceConstant.User.GET_USER_BY_ID)
    UserDto getById(@PathVariable("id") Long id);
}
