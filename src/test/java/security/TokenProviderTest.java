package security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.security.TokenProvider;
import security.security.UserPrincipal;
import java.util.List;

public class TokenProviderTest {

    private final TokenProvider tokenProvider = Mockito.mock(TokenProvider.class);
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void init() {
        List<GrantedAuthority> ts = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        userPrincipal = new UserPrincipal(1L, "test@abv.bg", "123", ts, true, "123");
    }

    @Test
    public void testMakeToken() {
        Mockito.when(tokenProvider.createToken(userPrincipal)).thenReturn("tokenCode");
        Assertions.assertEquals("tokenCode", tokenProvider.createToken(userPrincipal));
    }

    @Test
    public void testGetUserIdFromToken() {

        Mockito.when(tokenProvider.getUserIdFromToken("tokenCode")).thenReturn(1L);

        Assertions.assertEquals(1L, tokenProvider.getUserIdFromToken("tokenCode"));
    }

    @Test
    public void validateTokenCorrect() {

        Mockito.when(tokenProvider.validateToken("tokenCode")).thenReturn(true);

        Assertions.assertTrue(tokenProvider.validateToken("tokenCode"));
    }

    @Test
    public void validateTokenWrong() {

        Mockito.when(tokenProvider.validateToken("wrongTokenCode")).thenReturn(false);

        Assertions.assertFalse(tokenProvider.validateToken("wrongTokenCode"));
    }
}
