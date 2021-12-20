package security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.exceptions.OAuth2AuthenticationProcessingException;
import security.security.oauth2.user.OAuth2UserInfo;
import security.security.oauth2.user.UserFactoryUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserFactoryUtilTest {

    private Map<String, Object> attributes;

    @BeforeEach
    public void init() {
        attributes = new ConcurrentHashMap<>();
    }

    @Test
    public void testAddFacebookUser() {

        OAuth2UserInfo facebook = UserFactoryUtil.getOAuth2UserInfo("FACEBOOK", attributes);

        Assertions.assertNotNull(facebook);
    }



    @Test
    public void testAddWrongUser() {

        OAuth2AuthenticationProcessingException wrongLogin = Assertions.assertThrows(OAuth2AuthenticationProcessingException.class, () -> {
            UserFactoryUtil.getOAuth2UserInfo("SOFIA", attributes);
        });
        Assertions.assertEquals("Sorry! Login with SOFIA is not supported yet.", wrongLogin.getMessage());
    }

}
