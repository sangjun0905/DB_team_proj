package team1.domain.region;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DistrictCode {
    private String id;
    private String cityId;
    private String districtName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
