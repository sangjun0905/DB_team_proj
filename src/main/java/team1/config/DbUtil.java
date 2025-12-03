package team1.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DbConfig(dbconfig.ini → 환경변수 → 기본값)에서 읽은 정보로 커넥션을 생성합니다.
 */
public class DbUtil {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 한 번만 로딩
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        DbConfig db = DbConfig.load();
        return DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPass());
    }
}
