package security.security;

import security.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import security.service.CacheService;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    private final CacheService cacheService;
    private final AppProperties appProperties;

    public TokenProvider(CacheService cacheService, AppProperties appProperties) {
        this.cacheService = cacheService;

        this.appProperties = appProperties;
    }

    public String createToken(UserPrincipal userPrincipal) {
        Date expiry = new Date();
        expiry.setTime(new Date().getTime() + appProperties.addDaysToDate(appProperties.getAuth().
                getTokenExpirationDays()));
        List<String> roles = userPrincipal.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        String token = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .claim("roles", roles)
                .claim("uniqueId", userPrincipal.getUniquePlayerId())
                .signWith(getKey(appProperties.getAuth().getTokenSecret()), SignatureAlgorithm.HS512)
                .compact();

        cacheService.putElementInCache(token, expiry, userPrincipal);
        return token;

    }

    public Long getUserIdFromToken(String token) {

        Claims claims = Jwts.parserBuilder().
                setSigningKey(getKey(appProperties.getAuth().getTokenSecret())).
                build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(getKey(appProperties.getAuth().getTokenSecret())).
                    build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }

    private Key getKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}