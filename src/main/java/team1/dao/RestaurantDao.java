package team1.dao;

import team1.config.DbUtil;
import team1.domain.restaurant.Restaurant;
import team1.domain.restaurant.RestaurantRoom;
import team1.domain.restaurant.asset.RestaurantPicture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 매장 관련 조회/조작을 담당하는 DAO.
 * - restaurant
 * - restaurant_room
 * - restaurant_picture
 *
 * (도메인 클래스 필드/세터 이름은 프로젝트에 맞게 수정 필요)
 */
public class RestaurantDao {

    // =========================================================
    // 1. 매장 기본 조회
    // =========================================================

    /**
     * restaurant.id 기준 단건 조회. 없으면 null.
     */
    public Restaurant findRestaurantById(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, user_id, `Field`, cnt_review, `Field2`, city_code_id " +
                        "FROM restaurant " +
                        "WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRestaurant(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 도시/구 단위로 매장 목록 조회.
     * city_code_id, district_id 기준으로 필터링하고 싶다면
     * restaurant + district_code 조인해서 쓰면 됨.
     *
     * 여기서는 city_code_id만 필터 예시로 사용.
     */
    public List<Restaurant> findRestaurantsByCity(String cityCodeId) throws SQLException {
        String sql =
                "SELECT id, user_id, `Field`, cnt_review, `Field2`, city_code_id " +
                        "FROM restaurant " +
                        "WHERE city_code_id = ?";

        List<Restaurant> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cityCodeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRestaurant(rs));
                }
            }
        }

        return result;
    }

    /**
     * 이름(또는 Field2)에 LIKE 검색을 적용한 매장 검색 예시.
     * 실제 컬럼 의미에 맞게 WHERE 절 수정해도 됨.
     */
    public List<Restaurant> searchRestaurantsByName(String nameKeyword) throws SQLException {
        String sql =
                "SELECT id, user_id, `Field`, cnt_review, `Field2`, city_code_id " +
                        "FROM restaurant " +
                        "WHERE `Field2` LIKE ?"; // 예: Field2를 상호명으로 쓴다고 가정

        List<Restaurant> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nameKeyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRestaurant(rs));
                }
            }
        }

        return result;
    }

    /**
     * ResultSet → Restaurant 도메인 객체 매핑.
     * Restaurant 클래스 구조에 맞게 세터 이름/타입은 맞춰서 수정.
     */
    private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setId(rs.getString("id"));
        r.setUserId(rs.getString("user_id"));
        // DDL상 컬럼 이름이 Field / Field2 라서 일단 그대로 씀.
        // 실제 도메인 필드명이 다르면 여기서 맞춰서 세팅.
        r.setField(rs.getFloat("Field"));      // 예: 평균 평점 같은 것
        r.setCntReview(rs.getInt("cnt_review"));
        r.setField2(rs.getString("Field2"));   // 예: 매장명/설명 등
        r.setCityCodeId(rs.getString("city_code_id"));
        return r;
    }


    // =========================================================
    // 2. 매장 룸(테이블) 조회
    // =========================================================

    /**
     * 특정 매장의 룸 목록 조회.
     */
    public List<RestaurantRoom> findRoomsByRestaurant(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, restaurant_id, name, capacity, room_type, " +
                        "       is_active, description " +
                        "FROM restaurant_room " +
                        "WHERE restaurant_id = ? " +
                        "ORDER BY capacity ASC";

        List<RestaurantRoom> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRestaurantRoom(rs));
                }
            }
        }

        return result;
    }

    private RestaurantRoom mapRestaurantRoom(ResultSet rs) throws SQLException {
        RestaurantRoom room = new RestaurantRoom();
        room.setId(rs.getString("id"));
        room.setRestaurantId(rs.getString("restaurant_id"));
        room.setName(rs.getString("name"));
        room.setCapacity(rs.getInt("capacity"));
        room.setRoomType(rs.getString("room_type"));
        room.setActive(rs.getBoolean("is_active"));
        room.setDescription(rs.getString("description"));
        return room;
    }


    // =========================================================
    // 3. 매장 사진 조회
    // =========================================================

    /**
     * 특정 매장 사진 목록 조회.
     */
    public List<RestaurantPicture> findPicturesByRestaurant(String restaurantId) throws SQLException {
        String sql =
                "SELECT id, restaurant_picture, restaurant_id " +
                        "FROM restaurant_picture " +
                        "WHERE restaurant_id = ?";

        List<RestaurantPicture> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRestaurantPicture(rs));
                }
            }
        }

        return result;
    }

    private RestaurantPicture mapRestaurantPicture(ResultSet rs) throws SQLException {
        RestaurantPicture pic = new RestaurantPicture();
        pic.setId(rs.getString("id"));
        pic.setRestaurantPicture(rs.getString("restaurant_picture"));
        pic.setRestaurantId(rs.getString("restaurant_id"));
        return pic;
    }

    // =========================================================
    // 4. (옵션) 키워드 기반 검색을 하고 싶다면 여기에 추가
    //  - restaurant_keyword, restaurant_keyword_mapping_table 조인해서 구현 가능
    // =========================================================
}
