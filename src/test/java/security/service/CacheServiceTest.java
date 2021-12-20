package security.service;

import org.mockito.Mockito;
import security.cache.CacheManagement;
import security.config.AppProperties;
import security.exceptions.TokenNotFoundException;
import security.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.web.dto.ValidatedUser;
import javax.servlet.http.Cookie;
import java.net.MalformedURLException;
import java.util.*;

class CacheServiceTest {
    private CacheService cacheService;
    @Autowired
    private CacheManagement cacheManager;
    private List<SimpleGrantedAuthority> roles;
    private final UserService userService = Mockito.mock(UserService.class);
    private String token;
    private String pass;
    private String email;
    private boolean isActive;
    private String uniqueId;


    @BeforeEach
    public void init() {
        uniqueId = "10";
        isActive = true;
        this.token = "token";
        this.pass = "123";
        this.email = "asw@abv.bg";
        AppProperties appProperties = new AppProperties();
        this.cacheManager = new CacheManagement(appProperties);
        this.cacheService = new CacheService(cacheManager, userService);
        this.roles = Collections.
                singletonList(new SimpleGrantedAuthority(
                        "ROLE_USER"
                ));
    }


    @Test
    public void testAddNewUserIntoCache() throws TokenNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);
        UserPrincipal userPrincipal =
                new UserPrincipal(1L, "asw@abv.bg", "123", authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache("test", date, userPrincipal);
        ValidatedUser userFromToken = cacheService.getUserFromToken("test");

        Assertions.assertEquals("asw@abv.bg", userFromToken.getEmail());

    }

    @Test
    public void testFindUserCacheSuccessful() {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        UserPrincipal userPrincipal =
                new UserPrincipal(1L, email, pass, authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache(token, date, userPrincipal);
        Assertions.assertTrue(cacheService.checkIfTokenExist(token));
    }

    @Test
    public void testFindUserCacheFail() {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        UserPrincipal userPrincipal =
                new UserPrincipal(1L, email, pass, authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache(token, date, userPrincipal);
        Assertions.assertFalse(cacheService.checkIfTokenExist("noUser"));
    }

    @Test
    public void testDeleteAllCacheFail() {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        UserPrincipal userPrincipal =
                new UserPrincipal(1L, email, pass, authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache(email, date, userPrincipal);
        cacheService.deleteAll();
        Assertions.assertEquals(0, cacheService.getCacheSize());
    }

    @Test
    public void testDeleteTokenFromCache() {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        UserPrincipal userPrincipal =
                new UserPrincipal(1L, email, pass, authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache(token, date, userPrincipal);
        cacheService.removeToken(token);
        Assertions.assertEquals(0, cacheService.getCacheSize());
    }

    @Test
    public void testDeleteWrongTokenFromCache() {
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        UserPrincipal userPrincipal =
                new UserPrincipal(1L, email, pass, authorities, isActive, uniqueId);
        Date date = new Date();
        cacheService.putElementInCache(token, date, userPrincipal);
        cacheService.removeToken("testWrong");
        Assertions.assertEquals(1, cacheService.getCacheSize());
    }

    @Test
    public void testUpdateLogoutCookieValue() throws MalformedURLException {
        Cookie cookie = cacheService.invalidateCookie("test", "http://localhost.com");
        Assertions.assertEquals("test", cookie.getValue());
    }
    @Test
    public void testUpdateLogoutCookieName() throws MalformedURLException {
        Cookie cookie = cacheService.invalidateCookie("test", "http://localhost.com");
        Assertions.assertEquals("token", cookie.getName());
    }
    @Test
    public void testUpdateLogoutCookieDomain() throws MalformedURLException {
        Cookie cookie = cacheService.invalidateCookie("test", "http://localhost.com");
        Assertions.assertEquals("localhost.com", cookie.getDomain());
    }
    @Test
    public void testUpdateLogoutCookiePath() throws MalformedURLException {
        Cookie cookie = cacheService.invalidateCookie("test", "http://localhost.com");
        Assertions.assertEquals("/", cookie.getPath());
    }
}