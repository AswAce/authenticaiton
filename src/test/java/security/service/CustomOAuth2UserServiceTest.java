package security.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import security.db.model.UserEntity;
import security.db.repository.UserRepository;
import security.security.oauth2.user.OAuth2UserInfo;
import security.security.oauth2.user.UserFactoryUtil;
import security.web.dto.UserDto;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomOAuth2UserServiceTest {

    private static final String EMAIL = "test@abv.bg";

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserDto userDto;
    private UserEntity entity;
    private final OAuth2UserRequest oAuth2UserRequest = Mockito.mock(OAuth2UserRequest.class);
    private OAuth2User oAuth2User;
    private final DefaultOAuth2UserService defaultOAuth2UserService = Mockito.mock(DefaultOAuth2UserService.class);


    @BeforeEach
    public void init() {

        userDto = new UserDto();
        userDto.setEmail(EMAIL);
        userDto.setName("test");
        userDto.setImageUrl("image");
        entity = new UserEntity();
        entity.setEmail(EMAIL);
        entity.setName("test");
        entity.setImageUrl("image");
        String userNameAttributeName = "id";
        Map<String, Object> userAttributes = new ConcurrentHashMap<>();
        userAttributes.put("first_name", "test");
        userAttributes.put("last_name", "testLast");
        userAttributes.put("name", "test testLast");
        userAttributes.put("email", EMAIL);
        userAttributes.put("picture", "image");
        userAttributes.put("id", "123");
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("SCOPE_email"));
        authorities.add(new SimpleGrantedAuthority("SCOPE_public_profile"));
        oAuth2User = new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
    }

    @Test
    public void testLoadUserName() {
        OAuth2UserInfo facebook = UserFactoryUtil.getOAuth2UserInfo("FACEBOOK", oAuth2User.getAttributes());
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Mockito.when(defaultOAuth2UserService.loadUser(oAuth2UserRequest)).thenReturn(oAuth2User);
        MockedStatic<UserFactoryUtil> utilities = Mockito.mockStatic(UserFactoryUtil.class);
        utilities.when((MockedStatic.Verification) UserFactoryUtil.
                getOAuth2UserInfo("FACEBOOK", oAuth2User.getAttributes())).thenReturn(facebook);
        utilities.close();
        Assertions.assertEquals(EMAIL,
                UserFactoryUtil.getOAuth2UserInfo("FACEBOOK", oAuth2User.getAttributes()).getEmail());
    }
}
