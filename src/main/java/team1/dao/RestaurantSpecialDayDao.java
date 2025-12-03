package team1.dao;

import team1.domain.restaurant.RestaurantSpecialDay;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantSpecialDayDao {
    void insert(Connection conn, RestaurantSpecialDay specialDay) throws SQLException;
}
