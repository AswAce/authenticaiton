package security;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;
import security.security.RestAuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationEntryPointTest {

    private final RestAuthenticationEntryPoint entryPoint = Mockito.mock(RestAuthenticationEntryPoint.class);
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final AuthenticationException e = Mockito.mock(AuthenticationException.class);

    @Test
    public void testVoidCommence() throws IOException {
        Mockito.doNothing().when(entryPoint).
                commence(ArgumentMatchers.isA(HttpServletRequest.class),
                        ArgumentMatchers.isA(HttpServletResponse.class),
                        ArgumentMatchers.isA(AuthenticationException.class));
        entryPoint.commence(request, response, e);
        Mockito.verify(entryPoint, Mockito.times(1)).commence(request, response, e);

    }
}
