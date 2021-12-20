package security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import security.db.model.RoleEntity;
import security.db.model.UserEntity;
import security.db.model.enums.AuthProvider;
import security.db.model.enums.RoleTypes;
import security.db.repository.UserRepository;
import security.exceptions.ResourceNotFoundException;
import security.security.CustomUserDetailsService;
import java.util.Optional;

public class CustomUserDetailsServiceTest {
    private static final String EMAIL = "test@abv.bg";
    private final CustomUserDetailsService customUserDetailsService =
            Mockito.mock(CustomUserDetailsService.class);
    private CustomUserDetailsService serviceUD;

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserEntity testUser;

    @BeforeEach
    public void init() {
        serviceUD = new CustomUserDetailsService(userRepository);
        RoleEntity roleEntity = new RoleEntity(RoleTypes.USER);
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail(EMAIL);
        testUser.setEmailVerified(true);
        testUser.setName("test test");
        testUser.setProvider(AuthProvider.facebook);
        testUser.setProviderId("1234");
        testUser.getRoles().add(roleEntity);
    }

    @Test
    public void testLoadUserByEmailFound() {
//        Mockito.when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(userPrincipal);
        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        UserDetails expectedUserDetails = serviceUD.loadUserByUsername(EMAIL);

        Assertions.assertEquals(EMAIL, expectedUserDetails.getUsername());

    }

    @Test
    public void testLoadUserByIdFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser));
        UserDetails expectedUserDetails = serviceUD.loadUserById(1L);

        Assertions.assertEquals(EMAIL, expectedUserDetails.getUsername());

    }

    @Test()
    public void testLoadUserByIdError() {
        Mockito.when(userRepository.findById(1L)).
                thenThrow(new ResourceNotFoundException("User", "id", 1L));
        UserDetails userDetails = customUserDetailsService.loadUserById(1L);
        Assertions.assertNull(userDetails);
    }

    @Test()
    public void testLoadUserByUserNameError() {
        Mockito.when(userRepository.findByEmail(EMAIL)).
                thenThrow(new UsernameNotFoundException("User not found with email : "
                        +
                        EMAIL));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertNull(userDetails);
    }

}
