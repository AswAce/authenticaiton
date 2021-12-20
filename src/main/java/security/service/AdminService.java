package security.service;

import org.springframework.stereotype.Service;
import security.db.model.UserEntity;
import security.db.repository.UserRepository;
import security.exceptions.UniquePlayerIdExistException;
import security.exceptions.UserNotFoundException;
import security.web.dto.admin.ApprovalInfoDto;
import security.web.dto.admin.ApprovedUserDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ApprovalInfoDto> getUsersForApproval() {
        List<ApprovalInfoDto> usersForApproval = new ArrayList<>();
        List<UserEntity> usersFromDb = this.userRepository.findAllByIsActive(false);

        usersFromDb.forEach(u -> usersForApproval.add(new ApprovalInfoDto(u.getImageUrl(), u.getName(), u.getEmail())));

        return usersForApproval;
    }

    public void approveUser(final ApprovedUserDto approvedUser) throws UserNotFoundException, UniquePlayerIdExistException {
        UserEntity byUniquePlayerId = this.userRepository.findByUniquePlayerId(approvedUser.getUniquePlayerId());
        if (byUniquePlayerId != null) {
            throw new UniquePlayerIdExistException("Unique player id is already mapped");
        }
        UserEntity user = this.userRepository.findByEmail(approvedUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email:%s does not exist.",
                        approvedUser.getEmail())));
        user.setActive(true);
        user.setUniquePlayerId(approvedUser.getUniquePlayerId());

        this.userRepository.save(user);

    }
}
