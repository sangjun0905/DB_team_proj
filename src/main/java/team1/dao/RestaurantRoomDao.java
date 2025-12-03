package team1.dao;

import team1.domain.restaurant.RestaurantRoom;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantRoomDao {
    void insert(Connection conn, RestaurantRoom room) throws SQLException;
}
