package team1.dao;

import team1.domain.restaurant.RestaurantUserRole;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantUserRoleDao {
    void insert(Connection conn, RestaurantUserRole role) throws SQLException;
}
