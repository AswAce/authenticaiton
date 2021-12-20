package security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.web.dto.ValidatedUser;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserValidationServiceTest {
    private static final String WRONG_JSON = "{\"active\":false,\"name\":null,\"email\":null,\"body\":null,\"imageUrl\":null}";

    @Autowired
    private final Authentication auth = Mockito.mock(Authentication.class);

    @Autowired
    private final UserValidationService userValidationService = Mockito.mock(UserValidationService.class);

    private static final String URL = "/fit/steps";

    @Test
    public void testGetAnonymousJson() {
        Map<String, Object> stringObjectMap = new ConcurrentHashMap<>();
        List<GrantedAuthority> ts = new ArrayList<>();
        ValidatedUser user = new ValidatedUser("test@abv.bg",
                "test", "imageTest",
                true, ts, stringObjectMap, "123");

        Mockito.when(userValidationService.createJson(user, URL, auth))
                .thenReturn(WRONG_JSON);

        Assertions.assertEquals(WRONG_JSON,
                userValidationService.createJson(user, URL, auth));
    }

    @Test
    public void testGetUserJson() {
        Map<String, Object> stringObjectMap = new ConcurrentHashMap<>();
        List<GrantedAuthority> ts = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        ValidatedUser user = new ValidatedUser("test@abv.bg",
                "test", "imageTest",
                true, ts, stringObjectMap, "123");
        String returnJson = "{\"active\":true,\"name\":test,\"email\":test@abv.bg,\"body\":null,\"imageUrl\":imageTest}";
        Mockito.when(userValidationService.createJson(user, URL, auth))
                .thenReturn(returnJson);

        Assertions.assertEquals(returnJson,
                userValidationService.createJson(user, URL, auth));
    }

}
