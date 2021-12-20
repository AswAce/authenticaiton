package security.cache.enteties;

import security.security.UserPrincipal;
import java.time.LocalDateTime;

public class CacheEntity {
    private final UserPrincipal userPrincipal;
    private final LocalDateTime expiryDate;

    public CacheEntity(UserPrincipal userPrincipal, LocalDateTime expiryDate) {
        this.userPrincipal = userPrincipal;
        this.expiryDate = expiryDate;
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
