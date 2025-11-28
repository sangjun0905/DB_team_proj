package team1.dao;

import org.apache.catalina.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private final Connection connection;

    // 간단하게 Connection을 생성자 주입받는 형태로 가정
    public UserDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * 유저 1명 DB에 INSERT
     */
    public void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO user (id, name, email, phone) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());

            ps.executeUpdate();
        }
    }

    public User findUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                return user;
            }
        }
        return null;
    }
    public User findUserByName(String name) throws SQLException { return null; }

}
