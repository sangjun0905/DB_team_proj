package team1.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/your_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 한 번만 로딩
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
