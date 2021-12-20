package security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;
import security.security.UserPrincipal;
import security.web.dto.UserDto;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

@Component
public class UserMapperSenderService {
    //    private static final String ADDRESS = System.getenv("CORE_ADDRESS");
    private static final String ADDRESS = "http://localhost:8080/auth/info";
    private final UserService userService;
    private final ObjectMapper mapper;
    private final CloseableHttpClient httpClient;

    public UserMapperSenderService(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
        this.httpClient = HttpClientBuilder.create().build();
    }

    public int sendNewUser(UserPrincipal userPrincipal) throws IOException {
        if (userPrincipal != null) {
            UserDto userDtoByEmail = userService.getUserDtoByEmail(userPrincipal.getEmail());
            HttpPost post = new HttpPost(ADDRESS);
            StringEntity postingString = new StringEntity(mapper.writeValueAsString(userDtoByEmail));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            CloseableHttpResponse execute = httpClient.execute(post);
            execute.close();
            return execute.getStatusLine().getStatusCode();
        }
        return -1;
    }
}
