package team1.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.dao.UserDao;
import team1.domain.common.UserRole;
import team1.domain.user.User;
import team1.dto.user.UserCreateRequest;
import team1.dto.user.UserUpdateRequest;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public String createUser(UserCreateRequest req) throws SQLException {
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(req.getEmail());
        u.setPasswordHash(req.getPasswordHash());
        u.setName(req.getName());
        u.setPhone(req.getPhone());
        u.setRole(UserRole.valueOf(req.getRole()));
        u.setDeleted(false);
        u.setCreatedAt(java.time.LocalDateTime.now());
        u.setUpdatedAt(u.getCreatedAt());
        userDao.insertUser(u);
        return u.getId();
    }

    public User getById(String id) throws SQLException {
        return userDao.findById(id);
    }

    public User getByEmail(String email) throws SQLException {
        return userDao.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(String id, UserUpdateRequest req) throws SQLException {
        userDao.updateUserProfile(id, req.getName(), req.getPhone());
    }

    @Transactional(rollbackFor = Exception.class)
    public void softDelete(String id) throws SQLException {
        userDao.softDeleteUser(id);
    }
}
