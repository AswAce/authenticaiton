package security.security.oauth2.handler;

import lombok.SneakyThrows;
import security.config.AppProperties;
import security.exceptions.BadRequestException;
import security.security.TokenProvider;
import security.service.UserMapperSenderService;
import security.security.UserPrincipal;
import security.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import security.service.UserService;
import security.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private final AppProperties appProperties;

    private final UserMapperSenderService userMapperSenderService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final UserService userService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                                              UserMapperSenderService userMapperSenderService, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, UserService userService) {
        super();
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.userMapperSenderService = userMapperSenderService;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response);


        if (response.isCommitted() && logger.isDebugEnabled()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);


        if (targetUrl.endsWith("/signup")) {
            targetUrl = targetUrl.replace("/signup", "");
            userMapperSenderService.sendNewUser((UserPrincipal) authentication.getPrincipal());
        } else {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            if (userPrincipal != null && userService.checkIfUserIsActive(userPrincipal.getEmail())) {
                String token = tokenProvider.createToken(userPrincipal);
                Cookie cookie = new Cookie("token", token);

                String domain = AppProperties.getSiteDomain(request.getRequestURL().toString());

                cookie.setDomain(domain);
                cookie.setPath("/");
                response.addCookie(cookie);
            } else {
                targetUrl = targetUrl + "?error=The admin has not yet approved your account";
            }
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isEmpty() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        return redirectUri.orElse(getDefaultTargetUrl());
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}