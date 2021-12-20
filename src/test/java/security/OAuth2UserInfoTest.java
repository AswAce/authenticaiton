package security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.security.oauth2.user.FacebookOAuth2UserInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OAuth2UserInfoTest {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    private final Map<String, Object> pictureObj = new ConcurrentHashMap<>();
    private final Map<String, Object> dataObj = new ConcurrentHashMap<>();
    private FacebookOAuth2UserInfo userFacebook;


    @BeforeEach
    public void init() {
        dataObj.put("url", "image");
        pictureObj.put("data", dataObj);
        attributes.put("id", 1);
        attributes.put("name", "test");
        attributes.put("email", "test@abv.bg");
        attributes.put("picture", pictureObj);
        userFacebook = new FacebookOAuth2UserInfo(attributes);
    }

    @Test
    public void testGetFBUserEmail() {
        Assertions.assertEquals("test@abv.bg", userFacebook.getEmail());
    }

    @Test
    public void testGetFBUsername() {
        Assertions.assertEquals("test", userFacebook.getName());
    }

    @Test
    public void testGetFBUserPicture() {
        Assertions.assertEquals("image", userFacebook.getImageUrl());
    }

    @Test
    public void testGetFBUserEmptyPicture() {
        dataObj.remove("url");
        Assertions.assertNull(userFacebook.getImageUrl());
    }

    @Test
    public void testGetAttributes() {

        Assertions.assertEquals(4, userFacebook.getAttributes().size());
    }
}
