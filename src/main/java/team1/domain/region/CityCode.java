package team1.domain.region;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CityCode {
    private String id;
    private String cityName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
