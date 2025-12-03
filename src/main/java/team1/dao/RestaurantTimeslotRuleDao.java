package team1.dao;

import team1.domain.timeslot.RestaurantTimeslotRule;

import java.sql.Connection;
import java.sql.SQLException;

public interface RestaurantTimeslotRuleDao {
    void insert(Connection conn, RestaurantTimeslotRule rule) throws SQLException;
}
