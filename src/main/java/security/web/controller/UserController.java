package security.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/user-data")
public class UserController {
    private final UserService userService;

    private final ObjectMapper mapper;

    public UserController(final UserService userService, final ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/team-images-url")
    public ResponseEntity<String> getTeamImages(@RequestBody List<String> playerUniqueIDs) throws JsonProcessingException {
        return new ResponseEntity<>(mapper.writeValueAsString(userService.getImagesForPlayers(playerUniqueIDs)),
                HttpStatus.OK);
    }
}
