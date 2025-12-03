package team1.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 단순한 파일 기반 DB 설정 로더.
 * 우선순위: dbconfig.ini (프로젝트 루트) -> 환경변수(DB_URL/USER/PASS) -> 기본값.
 *
 * dbconfig.ini 예시:
 * db.url=jdbc:mysql://localhost:3306/my_catchtable?serverTimezone=Asia/Seoul
 * db.user=root
 * db.pass=password
 */
public class DbConfig {
    private final String url;
    private final String user;
    private final String pass;

    private DbConfig(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public static DbConfig load() {
        Properties props = new Properties();
        Path ini = Path.of("dbconfig.ini");
        if (Files.isReadable(ini)) {
            try (FileInputStream fis = new FileInputStream(ini.toFile())) {
                props.load(fis);
            } catch (IOException ignored) {
                // 파일이 있더라도 읽기 실패 시 아래 환경변수/기본값 사용
            }
        }

        String url = props.getProperty("db.url",
                System.getenv().getOrDefault("DB_URL",
                        "jdbc:mysql://localhost:3306/my_catchtable?serverTimezone=Asia/Seoul"));
        String user = props.getProperty("db.user",
                System.getenv().getOrDefault("DB_USER", "root"));
        String pass = props.getProperty("db.pass",
                System.getenv().getOrDefault("DB_PASS", "password"));

        return new DbConfig(url, user, pass);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
