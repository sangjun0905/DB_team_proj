package team1.dao;

import team1.domain.menu.RestaurantMenuOptionGroup;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantMenuOptionGroupDao {
    void insert(Connection conn, RestaurantMenuOptionGroup optionGroup) throws SQLException;
}
