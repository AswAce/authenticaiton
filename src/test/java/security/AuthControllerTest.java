package security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import security.db.model.enums.AuthProvider;
import security.db.model.enums.RoleTypes;
import security.db.model.RoleEntity;
import security.db.model.UserEntity;
import security.db.repository.RoleRepository;
import security.db.repository.UserRepository;
import security.security.UserPrincipal;
import security.service.CacheService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.util.Date;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import security.web.controller.AuthController;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String AUTH_CONTROLLER_PREFIX = "/auth/validate";

    @Autowired
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController()).build();
    @Autowired
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    @Autowired
    private final RoleRepository mockRoleRep = Mockito.mock(RoleRepository.class);
    @Autowired
    private final CacheService cacheService = Mockito.mock(CacheService.class);

    private UserEntity testUser;
    private UserPrincipal userPrincipal;

    private static final String EMAIL = "test@abv.bg";

    @BeforeEach
    public void setUp() {
        this.init();

    }

    @AfterEach
    public void delete() {
        mockUserRepository.deleteAll();
        mockRoleRep.deleteAll();
        cacheService.deleteAll();
    }

    private void init() {

        RoleEntity roleEntity = new RoleEntity(RoleTypes.USER);
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail(EMAIL);
        testUser.setEmailVerified(true);
        testUser.setName("test test");
        testUser.setProvider(AuthProvider.facebook);
        testUser.setProviderId("1234");
        testUser.setActive(true);
        testUser.setUniquePlayerId("123");
        mockRoleRep.save(roleEntity);
        mockUserRepository.save(testUser);
        testUser.getRoles().add(roleEntity);
        userPrincipal = UserPrincipal.create(mockUserRepository.findById(1L).orElse(testUser));
        Date date = new Date();
        cacheService.putElementInCache("test", date, userPrincipal);
    }


    @Test
    public void testUnauthorizedRequest()
            throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                        get(AUTH_CONTROLLER_PREFIX).contentType(MediaType.APPLICATION_JSON)).
                andReturn().getRequest();

    }

    @Test
    @WithMockUser(username = "username", roles = "OPERATOR")
    public void testRequestWithParamOperator()
            throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.
                        get(AUTH_CONTROLLER_PREFIX).cookie(new Cookie("token", "test")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.header().doesNotExist("X-Forwarded-User"));

    }

    @Test
    @WithMockUser(username = EMAIL, password = "123", roles = "USER")
    @WithUserDetails()
    public void testAuthorizedRequestWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get(AUTH_CONTROLLER_PREFIX, userPrincipal).
                        cookie(new Cookie("token", "test"))
                        .header("X-Original-URI", "/fit/steps")
                        .param("email", EMAIL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = EMAIL, password = "123", roles = "USER")
    @WithUserDetails()
    @Test
    public void testWrongTokenRequestWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get(AUTH_CONTROLLER_PREFIX, userPrincipal).
                        cookie(new Cookie("token", "aatest"))
                        .header("X-Original-URI", "/fit/steps")
                        .param("email", EMAIL))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @WithMockUser(username = "EMAIL", password = "123", roles = "USER")
    @WithUserDetails()
    @Test
    public void testWrongUserNameRequestWithUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        get(AUTH_CONTROLLER_PREFIX, userPrincipal).
                        cookie(new Cookie("token", "aatest"))
                        .header("X-Original-URI", "/fit/steps")
                        .param("email", EMAIL))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = EMAIL, password = "123", roles = "USER")
    @WithUserDetails()
    public void testLogoutUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                        post("/auth/logout", userPrincipal).
                        cookie(new Cookie("token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogoutUserWithTokenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout").
                        cookie(new Cookie("token","token"))
                ).
                andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testLogoutFakeUserNoToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")).
                andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
