package team1.service.timeslot;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team1.dao.TimeslotDao;
import team1.domain.timeslot.RestaurantTimeslotRule;
import team1.domain.timeslot.TimeslotInstance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class TimeslotGenerationService {

    private final TimeslotDao timeslotDao;

    public TimeslotGenerationService(TimeslotDao timeslotDao) {
        this.timeslotDao = timeslotDao;
    }

    /**
     * 지정한 날짜부터 days일 동안 타임슬롯 인스턴스를 생성한다.
     * 이미 있는 슬롯은 건너뛰고, 중복 insert 시 0건 처리.
     */
    @Transactional(rollbackFor = Exception.class)
    public int generate(String restaurantId, LocalDate startDate, int days) throws Exception {
        List<RestaurantTimeslotRule> rules = timeslotDao.findRulesByRestaurant(restaurantId);
        if (rules.isEmpty()) {
            return 0;
        }
        int created = 0;
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HHmm");

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            int dow = date.getDayOfWeek().getValue() % 7; // 1=Mon..7=Sun => 0=Sun..6=Sat
            if (dow == 0) dow = 7;
            int dowZeroBased = dow % 7; // Sun=0, Mon=1 ...

            for (RestaurantTimeslotRule rule : rules) {
                if (rule.getDayOfWeek() != dowZeroBased) continue;
                if (rule.isHoliday()) continue;

                LocalTime t = rule.getOpenTime();
                LocalTime close = rule.getCloseTime();
                while (t.isBefore(close)) {
                    TimeslotInstance ts = new TimeslotInstance();
                    ts.setId("TS-" + date.format(dateFmt) + "-" + t.format(timeFmt));
                    ts.setRuleId(rule.getId());
                    ts.setDate(date);
                    ts.setStartTime(t);
                    ts.setTeamCapacity(rule.getTeamCapacity());
                    ts.setReservedTeam(0);
                    ts.setActive(true);
                    created += timeslotDao.insertTimeslotInstance(ts);

                    t = t.plusMinutes(rule.getSlotInterval());
                }
            }
        }
        return created;
    }
}
