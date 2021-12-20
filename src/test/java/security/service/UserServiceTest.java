package security.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import security.db.model.UserEntity;
import security.db.repository.UserRepository;
import security.web.dto.UserDto;
import java.util.Optional;

public class UserServiceTest {

    private UserService userService;
    private final UserService mockUserService = Mockito.mock(UserService.class);
    private static final String EMAIL = "test@abv.bg";
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserDto userDto;
    private UserEntity entity;

    @BeforeEach
    public void init() {
        userDto = new UserDto();
        userDto.setEmail(EMAIL);
        userDto.setName("test");
        userDto.setImageUrl("image");
        userService = new UserService(userRepository);
        entity = new UserEntity();
        entity.setEmail(EMAIL);
        entity.setName("test");
        entity.setImageUrl("image");
    }

    @AfterEach
    public void after() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindUsernameByEmail() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        String usernameByEmail = userService.findUsernameByEmail(EMAIL);
        Assertions.assertEquals("test", usernameByEmail);
    }

    @Test
    public void testFindUsernameByEmailWrongEmail() {

        Mockito.when(userRepository.findByEmail("testWrong@abv.bg")).thenReturn(Optional.ofNullable(null));
        Assertions.assertNull(userService.findUsernameByEmail("testWrong@abv.bg"));
    }

    @Test
    public void testFindImageUrlByEmail() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        String usernameByEmail = userService.findImageUrlByEmail(EMAIL);
        Assertions.assertEquals("image", usernameByEmail);
    }

    @Test
    public void testFindImageUrlByEmailWrongEmail() {
        Mockito.when(userRepository.findByEmail("testWrong@abv.bg")).thenReturn(Optional.ofNullable(null));
        String usernameByEmail = userService.findUsernameByEmail("testWrong@abv.bg");
        Assertions.assertNull(usernameByEmail);
    }


    @Test
    public void testActivateUserVoid() {

        Mockito.doNothing().when(mockUserService).activateUser(ArgumentMatchers.isA(String.class));
        mockUserService.activateUser(EMAIL);
        Mockito.verify(mockUserService,
                Mockito.times(1)).activateUser(EMAIL);
    }

    @Test
    public void testGetUserDtoByEmailFindUsername() {
        userRepository.save(entity);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Assertions.assertEquals("test", userService.getUserDtoByEmail(EMAIL).getName());
    }

    @Test
    public void testGetUserDtoByEmailFindUserEmail() {
        userRepository.save(entity);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Assertions.assertEquals(EMAIL, userService.getUserDtoByEmail(EMAIL).getEmail());
    }

    @Test
    public void testGetUserDtoByEmailFindUserImageURL() {
        userRepository.save(entity);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Assertions.assertEquals("image", userService.getUserDtoByEmail(EMAIL).getImageUrl());
    }

    @Test
    public void testGetUserDtoByEmailReturnNull() {
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(null));
        Assertions.assertNull(userService.getUserDtoByEmail(EMAIL));
    }

    @Test
    public void testCheckIfUserIsActive() {
        entity.setActive(true);
        userRepository.save(entity);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Assertions.assertTrue(userService.checkIfUserIsActive(EMAIL));
    }

    @Test
    public void testCheckIfUserIsActiveFalse() {
        entity.setActive(false);
        userRepository.save(entity);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        Assertions.assertFalse(userService.checkIfUserIsActive(EMAIL));
    }
}
