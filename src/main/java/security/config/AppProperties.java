package security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationDays() {
            return
                    TimeUnit.MILLISECONDS.toDays(tokenExpirationMsec);
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }
    }

    public static final class OAuth2 {
        private final List<String> authorizedRedirectUris = new ArrayList<>();

        public List<String> getAuthorizedRedirectUris() {
            return authorizedRedirectUris;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public OAuth2 getOauth2() {
        return oauth2;
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Cookie cookie = Arrays.stream(request.getCookies()).
                    filter(cookie1 -> "token".equals(cookie1.getName())).
                    findFirst().orElse(null);
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public long addDaysToDate(long days) {
        return days * 1000 * 60 * 60 * 24;
    }

    public static String getSiteDomain(String requestUri) throws MalformedURLException {
        URL uri = new URL(requestUri);
        return uri.getHost();
    }
}