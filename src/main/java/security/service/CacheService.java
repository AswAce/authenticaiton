package security.service;

import security.cache.CacheManagement;
import security.config.AppProperties;
import security.exceptions.TokenNotFoundException;
import security.security.UserPrincipal;
import org.springframework.stereotype.Service;
import security.web.dto.ValidatedUser;

import javax.servlet.http.Cookie;
import java.net.MalformedURLException;
import java.util.Date;

@Service
public class CacheService {

    private final CacheManagement cacheManager;
    private final UserService userService;

    public CacheService(CacheManagement cacheManager, UserService userService) {
        this.cacheManager = cacheManager;
        this.userService = userService;
    }

    public void logOutUSerFromCache(String token) {

        cacheManager.removeValue(token);
    }

    public ValidatedUser getUserFromToken(String token) throws TokenNotFoundException {

        UserPrincipal loginUserIdFromToken = cacheManager.getLoginUserIdFromToken(token);

        return new ValidatedUser(loginUserIdFromToken.getEmail(),
                userService.findUsernameByEmail(loginUserIdFromToken.getEmail()),
                userService.findImageUrlByEmail(loginUserIdFromToken.getEmail()),
                userService.checkIfUserIsActive(loginUserIdFromToken.getEmail()),
                loginUserIdFromToken.getAuthorities(),
                loginUserIdFromToken.getAttributes(),
                loginUserIdFromToken.getUniquePlayerId());
    }

    public void deleteAll() {
        cacheManager.cleanWholeCache();
    }

    public void putElementInCache(String token, Date expireDate, UserPrincipal user) {
        cacheManager.putElement(token, expireDate, user);
    }

    public boolean checkIfTokenExist(String token) {
        return cacheManager.checkIfCacheContainsKey(token);
    }

    public int getCacheSize() {
        return cacheManager.getCacheSize();
    }

    public void removeToken(String token) {
        cacheManager.removeValue(token);
    }
    public void removeExpiresTokens() {
        cacheManager.removeExpireValues();
    }

    public Cookie invalidateCookie(String token, String requestURI) throws MalformedURLException {
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(0);
        cookie.setDomain(AppProperties.getSiteDomain(requestURI));
        cookie.setPath("/");
        return cookie;
    }
}
