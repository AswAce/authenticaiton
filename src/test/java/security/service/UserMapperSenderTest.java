package security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import security.security.UserPrincipal;
import security.web.dto.UserDto;
import java.io.IOException;
import java.util.List;


public class UserMapperSenderTest {
    private final UserService service = Mockito.mock(UserService.class);
    private final UserMapperSenderService userMapperSenderService = Mockito.mock(UserMapperSenderService.class);

    @Test
    public void testSendCorectInfoForNewUSer() throws IOException {
        UserDto userDto = new UserDto("test@abv.bg", "test", "image");
        List<GrantedAuthority> ts = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserPrincipal userPrincipal = new UserPrincipal(1L, "test@abv.bg",
                "123", ts, false, "123");
        Mockito.when(service.getUserDtoByEmail("test@abv.bg")).thenReturn(userDto);
        Mockito.when(userMapperSenderService.sendNewUser(userPrincipal)).thenReturn(200);

        Assertions.assertEquals(200, userMapperSenderService.sendNewUser(userPrincipal));

    }

    @Test
    public void testSendNullInfoForNewUSer() throws IOException {

        UserPrincipal userPrincipal = null;

        Mockito.when(userMapperSenderService.sendNewUser(userPrincipal)).thenReturn(-1);

        Assertions.assertEquals(-1, userMapperSenderService.sendNewUser(userPrincipal));

    }
}
