package info.manhhdtop.cloud.common.security.dtos;

import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import info.manhhdtop.cloud.common.core.dtos.UserWithRoleDto;
import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserPrincipal implements UserDetails {
    private Long id;
    private String email;
    private UserStatus status;
    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Long id, String email, UserStatus status, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.authorities = authorities;
    }

    public static UserPrincipal create(UserWithRoleDto user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(RoleDto::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getStatus(),
                authorities
        );
    }

    @Override
    @NonNull
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == UserStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}

