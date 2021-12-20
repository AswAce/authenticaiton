package security;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;
import security.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2AuthenticationFailureHandlerTest {

    @Mock
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler = Mockito.mock(OAuth2AuthenticationFailureHandler.class);
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException exception;

    @Test
    public void testOnAuthenticationFailure() throws IOException {

        Mockito.doNothing().when(oAuth2AuthenticationFailureHandler).
                onAuthenticationFailure(ArgumentMatchers.isA(HttpServletRequest.class),
                        ArgumentMatchers.isA(HttpServletResponse.class),
                        ArgumentMatchers.isA(AuthenticationException.class));

        oAuth2AuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
        Mockito.verify(oAuth2AuthenticationFailureHandler,
                Mockito.times(1)).onAuthenticationFailure(request, response, exception);

    }
}
