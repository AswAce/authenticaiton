package security.security;

import security.db.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.*;
import java.util.stream.Collectors;

public class UserPrincipal implements OAuth2User, UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private final boolean isActive;
    private final String uniquePlayerId;

    //to add if account is locked

    public UserPrincipal(Long id,
                         String email,
                         String password,
                         Collection<? extends GrantedAuthority> authorities,
                         boolean isActive,
                         String uniquePlayerId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
        this.uniquePlayerId = uniquePlayerId;
    }

    public static UserPrincipal create(UserEntity user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().
                map(roleEntity -> new SimpleGrantedAuthority(
                        "ROLE_" + roleEntity.getRole().name()
                )).collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                "pass",
                authorities,
                user.getIsActive(),
                user.getUniquePlayerId());
    }

    public static UserPrincipal create(UserEntity user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    public String getUniquePlayerId() {
        return this.uniquePlayerId;
    }
}
