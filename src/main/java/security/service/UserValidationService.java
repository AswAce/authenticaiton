package security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import security.web.dto.ValidatedUser;

@Service
public class UserValidationService {
    @Autowired
    private WebInvocationPrivilegeEvaluator evaluator;
    private Authentication authentication;

    public String createJson(ValidatedUser user, String url, Authentication authentication) {
        setAuthentication(authentication);
        boolean checkUserAuthentication = checkUserAuthentication(url);
        if (!checkUserAuthentication) {
            return "";
        }

        return user.getUniquePlayerId();
    }

    private void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    private boolean checkUserAuthentication(String url) {
        if (url == null) {
            return false;
        }
        return evaluator.isAllowed(url, authentication);
    }
}
