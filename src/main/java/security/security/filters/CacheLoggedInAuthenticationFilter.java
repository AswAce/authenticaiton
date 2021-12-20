package security.security.filters;

import lombok.SneakyThrows;
import security.config.AppProperties;
import security.exceptions.OAuth2AuthenticationProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheLoggedInAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AppProperties appProperties;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            appProperties.getJwtFromRequest(request);
        } catch (OAuth2AuthenticationProcessingException e) {
            throw e.fillInStackTrace();

        }
        filterChain.doFilter(request, response);
    }
}


