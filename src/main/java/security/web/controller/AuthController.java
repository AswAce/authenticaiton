package security.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import security.config.AppProperties;
import security.exceptions.TokenNotFoundException;
import security.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.service.UserValidationService;
import security.web.dto.ValidatedUser;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserValidationService userValidationService;

    @PostMapping("/logout")
    public ResponseEntity logoutUser(@CookieValue(name = "token", required = false) String token, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
        if (token == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        cacheService.logOutUSerFromCache(appProperties.getJwtFromRequest(request));

        Cookie cookie = cacheService.invalidateCookie(token, request.getRequestURL().toString());
        response.addCookie(cookie);

        return new ResponseEntity(cookie, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@CookieValue(name = "token", required = false) String token,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ValidatedUser user;
        try {
            user = this.cacheService.getUserFromToken(token);
        } catch (TokenNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uniquePlayerId = userValidationService.createJson(user,
                request.getHeader("X-Original-URI"),
                authentication);

        if (uniquePlayerId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        response.addHeader("X-Forwarded-User", user.getUniquePlayerId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}