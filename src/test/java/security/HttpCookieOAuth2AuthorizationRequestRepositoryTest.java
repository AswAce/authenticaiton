package security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import security.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;

import javax.servlet.http.HttpServletRequest;


public class HttpCookieOAuth2AuthorizationRequestRepositoryTest {
    private final HttpCookieOAuth2AuthorizationRequestRepository repo =
            Mockito.mock(HttpCookieOAuth2AuthorizationRequestRepository.class);
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    @Test
    public void testLoadAuthorizationRequestNull() {

        OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
                repo.loadAuthorizationRequest(request);
        Assertions.assertNull(oAuth2AuthorizationRequest);
    }

    @Test
    public void testRemoveAuthorizationRequest() {
        Assertions.assertNull(repo.removeAuthorizationRequest(request));
    }
}
