package security.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.config.AppProperties;
import security.exceptions.TokenNotFoundException;
import security.service.CacheService;
import security.service.UserService;
import security.web.dto.ValidatedUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user-data")
public class UserController {
    private final UserService userService;

    private final ObjectMapper mapper;

    private final AppProperties properties;

    private final CacheService cacheService;

    public UserController(final UserService userService, final ObjectMapper mapper, AppProperties properties, CacheService cacheService) {
        this.userService = userService;
        this.mapper = mapper;
        this.properties = properties;
        this.cacheService = cacheService;
    }

    @PostMapping("/team-images-url")
    public ResponseEntity<String> getTeamImages(@RequestBody List<String> playerUniqueIDs) throws JsonProcessingException {
        return new ResponseEntity<>(mapper.writeValueAsString(userService.getImagesForPlayers(playerUniqueIDs)),
                HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getPRofile(HttpServletRequest request) {
        String jwtFromRequest = properties.getJwtFromRequest(request);

        try {
            ValidatedUser userFromToken = cacheService.getUserFromToken(jwtFromRequest);
            return new ResponseEntity(userFromToken, HttpStatus.OK);
        } catch (TokenNotFoundException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


    }
}
