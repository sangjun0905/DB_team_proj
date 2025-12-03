package team1.dao;

import team1.domain.menu.RestaurantMenu;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantMenuDao {
    void insert(Connection conn, RestaurantMenu menu) throws SQLException;
}
