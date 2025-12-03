package team1.dao;

import team1.config.DbUtil;
import team1.domain.user.User;
import team1.domain.common.UserRole;

import java.sql.*;

public class UserDao {

    // 회원 가입
    public void insertUser(User u) throws SQLException {
        String sql = """
            INSERT INTO user (
                id, email, password_hash, name, phone,
                role, is_deleted, created_at, updated_at
            ) VALUES (
                ?, ?, ?, ?, ?,
                ?, 0, NOW(), NOW()
            )
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getId());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getName());
            ps.setString(5, u.getPhone());
            ps.setString(6, u.getRole().name());

            ps.executeUpdate();
        }
    }

    // 로그인용: 이메일로 조회
    public User findByEmail(String email) throws SQLException {
        String sql = """
            SELECT *
            FROM user
            WHERE email = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    // 일반 PK 기반 조회
    public User findById(String id) throws SQLException {
        String sql = """
            SELECT *
            FROM user
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    // 프로필 수정 (이름/전화번호 정도만)
    public void updateUserProfile(String id, String name, String phone) throws SQLException {
        String sql = """
            UPDATE user
            SET name = ?,
                phone = ?,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, id);
            ps.executeUpdate();
        }
    }

    // 비밀번호 변경
    public void updatePassword(String id, String newPasswordHash) throws SQLException {
        String sql = """
            UPDATE user
            SET password_hash = ?,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPasswordHash);
            ps.setString(2, id);
            ps.executeUpdate();
        }
    }

    // 회원 탈퇴 (soft delete) – 트리거가 deleted_at 채워줌
    public void softDeleteUser(String id) throws SQLException {
        String sql = """
            UPDATE user
            SET is_deleted = 1,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    // 내부 매핑 함수
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getString("id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setName(rs.getString("name"));
        u.setPhone(rs.getString("phone"));
        u.setRole(UserRole.valueOf(rs.getString("role")));
        u.setDeleted(rs.getBoolean("is_deleted"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        u.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        Timestamp deletedAt = rs.getTimestamp("deleted_at");
        if (deletedAt != null) {
            u.setDeletedAt(deletedAt.toLocalDateTime());
        }
        return u;
    }
}
