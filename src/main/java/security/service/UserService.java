package security.service;

import org.springframework.stereotype.Service;
import security.db.model.UserEntity;
import security.db.repository.UserRepository;
import security.web.dto.PlayerImageUrlDto;
import security.web.dto.UserDto;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String findUsernameByEmail(String email) {

        UserEntity byEmail = userRepository.
                findByEmail(email).
                orElse(null);
        if (byEmail != null) {
            return byEmail.getName();
        }
        return null;
    }

    public String findImageUrlByEmail(String email) {

        UserEntity byEmail = userRepository.
                findByEmail(email).
                orElse(null);
        if (byEmail != null) {
            return byEmail.getImageUrl();
        }
        return null;

    }


    public boolean checkIfUserIsActive(String email) {
        UserEntity byEmail = userRepository.findByEmail(email).orElse(null);
        if (byEmail == null) {
            return false;
        }
        return byEmail.getIsActive();

    }

    public void activateUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    public UserDto getUserDtoByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return new UserDto(user.getEmail(), user.getName(), user.getImageUrl());
        }
        return null;
    }

    public List<PlayerImageUrlDto> getImagesForPlayers(List<String> playerUniqueIDs) {
        List<PlayerImageUrlDto> playerImageUrlDtos = playerUniqueIDs.stream().
                map(playerUniqueId -> {
                    UserEntity byUniquePlayerId = userRepository.findByUniquePlayerId(playerUniqueId);
                    if (byUniquePlayerId == null) {
                        return null;
                    }
                    return new PlayerImageUrlDto(byUniquePlayerId.getUniquePlayerId(), byUniquePlayerId.getImageUrl());
                }).toList();
        if (countNullPlayers(playerImageUrlDtos)) {
            playerImageUrlDtos = new ArrayList<>();
        }

        return playerImageUrlDtos;
    }

    private boolean countNullPlayers(List<PlayerImageUrlDto> playerImageUrlDtos) {
        int count = (int) playerImageUrlDtos.stream().
                filter(playerImageUrlDto -> playerImageUrlDto == null).count();
        return count >= playerImageUrlDtos.size();
    }
}
