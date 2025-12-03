package team1.dao;

import team1.domain.menu.RestaurantMenuOption;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantMenuOptionDao {
    void insert(Connection conn, RestaurantMenuOption option) throws SQLException;
}
