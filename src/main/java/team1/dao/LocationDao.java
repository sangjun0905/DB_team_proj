package team1.dao;

import team1.config.DbUtil;
// 도메인 클래스 패키지는 네 프로젝트 구조에 맞게 바꿔줘
import team1.domain.location.CityCode;
import team1.domain.location.DistrictCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 지역 코드 (city_code, district_code) 관련 DAO.
 * - 도시 목록 조회
 * - 특정 도시의 구/군 목록 조회
 * - id / 코드 값으로 단건 조회
 */
public class LocationDao {

    // =========================================================
    // 1. city_code 관련
    // =========================================================

    /**
     * 전체 도시 목록 조회.
     */
    public List<CityCode> findAllCities() throws SQLException {
        String sql = """
            SELECT id, city_code
            FROM city_code
            ORDER BY city_code
            """;

        List<CityCode> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapCityCode(rs));
            }
        }

        return result;
    }

    /**
     * city_code 문자열(예: "SEOUL") 기준 단건 조회.
     * 없으면 null.
     */
    public CityCode findCityByCode(String cityCode) throws SQLException {
        String sql = """
            SELECT id, city_code
            FROM city_code
            WHERE city_code = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cityCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCityCode(rs);
                }
            }
        }

        return null;
    }

    /**
     * city_code PK(id) 기준 단건 조회. 없으면 null.
     */
    public CityCode findCityById(String cityId) throws SQLException {
        String sql = """
            SELECT id, city_code
            FROM city_code
            WHERE id = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cityId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCityCode(rs);
                }
            }
        }

        return null;
    }

    private CityCode mapCityCode(ResultSet rs) throws SQLException {
        CityCode c = new CityCode();
        c.setId(rs.getString("id"));
        c.setCityCode(rs.getString("city_code"));
        return c;
    }

    // =========================================================
    // 2. district_code 관련
    // =========================================================

    /**
     * 특정 도시(city_code.id)의 구/군 목록 조회.
     */
    public List<DistrictCode> findDistrictsByCityId(String cityId) throws SQLException {
        String sql = """
            SELECT id, city_code_id, district
            FROM district_code
            WHERE city_code_id = ?
            ORDER BY district
            """;

        List<DistrictCode> result = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cityId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapDistrictCode(rs));
                }
            }
        }

        return result;
    }

    /**
     * 구/군 PK(id) 기준 단건 조회. 없으면 null.
     */
    public DistrictCode findDistrictById(String districtId) throws SQLException {
        String sql = """
            SELECT id, city_code_id, district
            FROM district_code
            WHERE id = ?
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, districtId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDistrictCode(rs);
                }
            }
        }

        return null;
    }

    private DistrictCode mapDistrictCode(ResultSet rs) throws SQLException {
        DistrictCode d = new DistrictCode();
        d.setId(rs.getString("id"));
        d.setCityCodeId(rs.getString("city_code_id"));
        d.setDistrict(rs.getString("district"));
        return d;
    }

    // 필요하면 여기다 district 이름으로 LIKE 검색하는 메서드도 추가 가능
}
