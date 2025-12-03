package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.menu.RestaurantMenu;
import team1.domain.menu.RestaurantMenuOption;
import team1.domain.menu.RestaurantMenuOptionGroup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MenuDao {

    private final DataSource dataSource;

    public MenuDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<RestaurantMenu> findMenusByRestaurant(String restaurantId) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant_menu
            WHERE restaurant_id = ?
              AND is_deleted = 0
              AND is_active = 1
            ORDER BY sort_order, name
            """;

        List<RestaurantMenu> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantMenu m = new RestaurantMenu();
                    m.setId(rs.getString("id"));
                    m.setRestaurantId(rs.getString("restaurant_id"));
                    m.setName(rs.getString("name"));
                    m.setPrice(rs.getInt("price"));
                    m.setDescription(rs.getString("description"));
                    m.setImageUrl(rs.getString("image_url"));
                    m.setSortOrder(rs.getInt("sort_order"));
                    m.setActive(rs.getBoolean("is_active"));
                    m.setDeleted(rs.getBoolean("is_deleted"));
                    m.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    m.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(m);
                }
            }
        }
        return list;
    }

    public List<RestaurantMenuOptionGroup> findGroupsByMenu(String menuId) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant_menu_option_group
            WHERE menu_id = ?
            ORDER BY name
            """;

        List<RestaurantMenuOptionGroup> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, menuId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantMenuOptionGroup g = new RestaurantMenuOptionGroup();
                    g.setId(rs.getString("id"));
                    g.setMenuId(rs.getString("menu_id"));
                    g.setName(rs.getString("name"));
                    g.setRequired(rs.getBoolean("is_required"));
                    g.setMaxSelect(rs.getInt("max_select"));
                    g.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    g.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(g);
                }
            }
        }
        return list;
    }

    public List<RestaurantMenuOption> findOptionsByGroup(String groupId) throws SQLException {
        String sql = """
            SELECT *
            FROM restaurant_menu_option
            WHERE option_group_id = ?
            ORDER BY name
            """;

        List<RestaurantMenuOption> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RestaurantMenuOption o = new RestaurantMenuOption();
                    o.setId(rs.getString("id"));
                    o.setOptionGroupId(rs.getString("option_group_id"));
                    o.setName(rs.getString("name"));
                    o.setPrice(rs.getInt("price"));
                    o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    o.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    list.add(o);
                }
            }
        }
        return list;
    }
}
