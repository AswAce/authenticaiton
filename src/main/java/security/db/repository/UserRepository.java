package security.db.repository;

import security.db.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<UserEntity> findAllByIsActiveFalse();

    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByImageUrl(String imageUrl);

    List<UserEntity> findAllByIsActive(boolean isActive);

    UserEntity findByUniquePlayerId(String playerUniqueId);
}
