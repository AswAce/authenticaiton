package security.cache;

import security.cache.enteties.CacheEntity;
import security.config.AppProperties;
import security.exceptions.TokenNotFoundException;
import security.security.UserPrincipal;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheManagement {
    private static final Map<String, CacheEntity> CACHE = new ConcurrentHashMap<>();

    private final AppProperties appProperties;

    public CacheManagement(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public void putElement(String key, Date expireDate, UserPrincipal user) {

        CACHE.putIfAbsent(key, new CacheEntity(
                user, appProperties.convertToLocalDateTimeViaInstant(expireDate)
        ));
    }

    public boolean checkIfCacheContainsKey(String key) {
        if (key != null) {
            return CACHE.containsKey(key);
        }
        return false;
    }

    public void removeValue(String key) {
        if (checkIfCacheContainsKey(key)) {
            CACHE.remove(key);
        }
    }

    public UserPrincipal getLoginUserIdFromToken(String token) throws TokenNotFoundException {
        if (!checkIfCacheContainsKey(token)) {
            throw new TokenNotFoundException("Token is wrong");
        }
        return CACHE.get(token).getUserPrincipal();
    }

    public void cleanWholeCache() {
        CACHE.clear();
    }

    //schedule cache cleaning
    public void removeExpireValues() {
        CACHE.forEach((k, cacheEntity) -> {
            if (cacheEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                removeValue(k);
            }
        });
    }

    public int getCacheSize() {
        return CACHE.size();
    }

}
