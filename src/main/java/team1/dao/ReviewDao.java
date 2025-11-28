package team1.dao;

import java.sql.Connection;

public class ReviewDao {
    private final Connection connection;

    // 간단하게 Connection을 생성자 주입받는 형태로 가정
    public ReviewDao(Connection connection) {
        this.connection = connection;
    }

}
