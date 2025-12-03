package team1.dao;

import org.springframework.stereotype.Repository;
import team1.domain.coupon.Coupon;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CouponDao {

    private final DataSource dataSource;

    public CouponDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 쿠폰 발급 (이미 생성된 template 기준)
    public void insertCoupon(Coupon c) throws SQLException {
        String sql = """
            INSERT INTO coupon (
                id, user_id, template_id,
                is_used, used_at,
                is_deleted, created_at, updated_at
            ) VALUES (
                ?, ?, ?,
                0, NULL,
                0, NOW(), NOW()
            )
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getId());
            ps.setString(2, c.getUserId());
            ps.setString(3, c.getTemplateId());
            ps.executeUpdate();
        }
    }

    public List<Coupon> findAvailableCouponsForUser(String userId, LocalDate onDate) throws SQLException {
        String sql = """
            SELECT c.*
            FROM coupon c
            JOIN coupon_template t ON c.template_id = t.id
            WHERE c.user_id = ?
              AND c.is_deleted = 0
              AND c.is_used = 0
              AND t.valid_from <= ?
              AND t.valid_until >= ?
            """;

        List<Coupon> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setDate(2, Date.valueOf(onDate));
            ps.setDate(3, Date.valueOf(onDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCoupon(rs));
                }
            }
        }
        return list;
    }

    public Coupon findById(String id) throws SQLException {
        String sql = """
            SELECT *
            FROM coupon
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCoupon(rs);
                }
            }
        }
        return null;
    }

    public void markUsed(String couponId, LocalDateTime usedAt) throws SQLException {
        String sql = """
            UPDATE coupon
            SET is_used = 1,
                used_at = ?,
                updated_at = NOW()
            WHERE id = ?
              AND is_deleted = 0
              AND is_used = 0
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(usedAt));
            ps.setString(2, couponId);
            ps.executeUpdate();
        }
    }

    public void softDelete(String couponId) throws SQLException {
        String sql = """
            UPDATE coupon
            SET is_deleted = 1,
                updated_at = NOW()
            WHERE id = ? AND is_deleted = 0
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, couponId);
            ps.executeUpdate();
        }
    }

    private Coupon mapCoupon(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setId(rs.getString("id"));
        c.setUserId(rs.getString("user_id"));
        c.setTemplateId(rs.getString("template_id"));
        c.setUsed(rs.getBoolean("is_used"));
        Timestamp usedAt = rs.getTimestamp("used_at");
        if (usedAt != null) {
            c.setUsedAt(usedAt.toLocalDateTime());
        }
        c.setDeleted(rs.getBoolean("is_deleted"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        Timestamp del = rs.getTimestamp("deleted_at");
        if (del != null) {
            c.setDeletedAt(del.toLocalDateTime());
        }
        return c;
    }
}
