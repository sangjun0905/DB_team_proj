package team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team1.domain.user.User;
import team1.domain.user.asset.UserPicture;

import java.util.List;
import java.util.UUID;

public interface UserPictureRepository extends JpaRepository<UserPicture, UUID> {
    List<UserPicture> findByUser(User user);
}
