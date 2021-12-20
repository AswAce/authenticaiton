package security.web.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public class ValidatedUser {

    private final String email;
    private final String name;
    private final String imageUrl;
    private final boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String uniquePlayerId;

    public ValidatedUser(String email,
                         String name,
                         String imageUrl,
                         boolean isActive,
                         Collection<? extends GrantedAuthority> authorities,
                         Map<String, Object> attributes,
                         String uniquePlayerId) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.authorities = authorities;
        this.attributes = attributes;
        this.uniquePlayerId = uniquePlayerId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getUniquePlayerId() {
        return uniquePlayerId;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
