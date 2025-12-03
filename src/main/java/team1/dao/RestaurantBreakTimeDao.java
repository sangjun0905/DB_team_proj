package team1.dao;

import team1.domain.restaurant.RestaurantBreakTime;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantBreakTimeDao {
    void insert(Connection conn, RestaurantBreakTime breakTime) throws SQLException;
}
