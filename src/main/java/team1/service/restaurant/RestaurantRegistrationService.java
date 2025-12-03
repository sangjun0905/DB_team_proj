package team1.service.restaurant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.datasource.DataSourceUtils;
import team1.dao.*;
import team1.domain.common.RestaurantStaffRole;
import team1.domain.menu.RestaurantMenu;
import team1.domain.menu.RestaurantMenuOption;
import team1.domain.menu.RestaurantMenuOptionGroup;
import team1.domain.restaurant.*;
import team1.domain.timeslot.RestaurantTimeslotRule;
import team1.dto.restaurant.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantRegistrationService {

    private final DataSource dataSource;
    private final RestaurantDao restaurantDao;
    private final RestaurantUserRoleDao restaurantUserRoleDao;
    private final RestaurantRoomDao restaurantRoomDao;
    private final RestaurantTimeslotRuleDao timeslotRuleDao;
    private final RestaurantBreakTimeDao breakTimeDao;
    private final RestaurantSpecialDayDao specialDayDao;
    private final RestaurantMenuDao restaurantMenuDao;
    private final RestaurantMenuOptionGroupDao optionGroupDao;
    private final RestaurantMenuOptionDao optionDao;

    public RestaurantRegistrationService(
            DataSource dataSource,
            RestaurantDao restaurantDao,
            RestaurantUserRoleDao restaurantUserRoleDao,
            RestaurantRoomDao restaurantRoomDao,
            RestaurantTimeslotRuleDao timeslotRuleDao,
            RestaurantBreakTimeDao breakTimeDao,
            RestaurantSpecialDayDao specialDayDao,
            RestaurantMenuDao restaurantMenuDao,
            RestaurantMenuOptionGroupDao optionGroupDao,
            RestaurantMenuOptionDao optionDao
    ) {
        this.dataSource = dataSource;
        this.restaurantDao = restaurantDao;
        this.restaurantUserRoleDao = restaurantUserRoleDao;
        this.restaurantRoomDao = restaurantRoomDao;
        this.timeslotRuleDao = timeslotRuleDao;
        this.breakTimeDao = breakTimeDao;
        this.specialDayDao = specialDayDao;
        this.restaurantMenuDao = restaurantMenuDao;
        this.optionGroupDao = optionGroupDao;
        this.optionDao = optionDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public String registerRestaurant(RestaurantRegistrationRequest request) throws RestaurantRegistrationException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            LocalDateTime now = LocalDateTime.now();
            String restaurantId = UUID.randomUUID().toString();

            // 1) restaurant
            RestaurantBasicDto basic = request.getRestaurant();
            Restaurant restaurant = new Restaurant();
            restaurant.setId(restaurantId);
            restaurant.setName(basic.getName());
            restaurant.setCityId(basic.getCityId());
            restaurant.setDistrictId(basic.getDistrictId());
            restaurant.setAddress(basic.getAddress());
            restaurant.setPhone(basic.getPhone());
            restaurant.setSupportsReservation(basic.isSupportsReservation());
            restaurant.setSupportsWaiting(basic.isSupportsWaiting());
            restaurant.setAvgRating(java.math.BigDecimal.ZERO);
            restaurant.setReviewCount(0);
            restaurant.setDeleted(false);
            restaurant.setCreatedAt(now);
            restaurant.setUpdatedAt(now);
            restaurantDao.insert(conn, restaurant);

            // 2) owner role
            RestaurantUserRole owner = new RestaurantUserRole();
            owner.setId(UUID.randomUUID().toString());
            owner.setRestaurantId(restaurantId);
            owner.setUserId(basic.getOwnerUserId());
            owner.setRole(RestaurantStaffRole.OWNER);
            owner.setCreatedAt(now);
            owner.setUpdatedAt(now);
            restaurantUserRoleDao.insert(conn, owner);

            // 3) rooms
            for (RoomDto r : safe(request.getRooms())) {
                RestaurantRoom room = new RestaurantRoom();
                room.setId(UUID.randomUUID().toString());
                room.setRestaurantId(restaurantId);
                room.setName(r.getName());
                room.setCapacity(r.getCapacity());
                room.setRoomType(r.getRoomType());
                room.setDescription(r.getDescription());
                room.setActive(true);
                room.setDeleted(false);
                room.setCreatedAt(now);
                room.setUpdatedAt(now);
                restaurantRoomDao.insert(conn, room);
            }

            // 4) timeslot rules
            for (TimeslotRuleDto t : safe(request.getTimeslotRules())) {
                RestaurantTimeslotRule rule = new RestaurantTimeslotRule();
                rule.setId(UUID.randomUUID().toString());
                rule.setRestaurantId(restaurantId);
                rule.setDayOfWeek(t.getDayOfWeek());
                rule.setOpenTime(t.getOpenTime());
                rule.setCloseTime(t.getCloseTime());
                rule.setSlotInterval(t.getSlotInterval());
                rule.setUsageTime(t.getUsageTime());
                rule.setTeamCapacity(t.getTeamCapacity());
                rule.setHoliday(t.isHoliday());
                rule.setAllowWaiting(t.isAllowWaiting());
                rule.setCreatedAt(now);
                rule.setUpdatedAt(now);
                timeslotRuleDao.insert(conn, rule);
            }

            // 5) break times
            for (BreakTimeDto b : safe(request.getBreakTimes())) {
                RestaurantBreakTime bt = new RestaurantBreakTime();
                bt.setId(UUID.randomUUID().toString());
                bt.setRestaurantId(restaurantId);
                bt.setDayOfWeek(b.getDayOfWeek() == null ? 0 : b.getDayOfWeek());
                bt.setBreakStartTime(b.getBreakStartTime());
                bt.setBreakEndTime(b.getBreakEndTime());
                bt.setApplyDate(b.getApplyDate());
                bt.setActive(b.isActive());
                bt.setMemo(b.getMemo());
                bt.setCreatedAt(now);
                bt.setUpdatedAt(now);
                breakTimeDao.insert(conn, bt);
            }

            // 6) special days
            for (SpecialDayDto s : safe(request.getSpecialDays())) {
                RestaurantSpecialDay sd = new RestaurantSpecialDay();
                sd.setId(UUID.randomUUID().toString());
                sd.setRestaurantId(restaurantId);
                sd.setDate(s.getDate());
                sd.setType(RestaurantSpecialDayType.valueOf(s.getType()));
                sd.setAllowReservation(s.isAllowReservation());
                sd.setAllowWaiting(s.isAllowWaiting());
                sd.setMemo(s.getMemo());
                sd.setCreatedAt(now);
                sd.setUpdatedAt(now);
                specialDayDao.insert(conn, sd);
            }

            // 7) menus & options
            for (MenuDto m : safe(request.getMenus())) {
                String menuId = UUID.randomUUID().toString();
                RestaurantMenu menu = new RestaurantMenu();
                menu.setId(menuId);
                menu.setRestaurantId(restaurantId);
                menu.setName(m.getName());
                menu.setPrice(m.getPrice());
                menu.setDescription(m.getDescription());
                menu.setImageUrl(m.getImageUrl());
                menu.setSortOrder(m.getSortOrder());
                menu.setActive(m.isActive());
                menu.setDeleted(false);
                menu.setCreatedAt(now);
                menu.setUpdatedAt(now);
                restaurantMenuDao.insert(conn, menu);

                for (OptionGroupDto g : safe(m.getOptionGroups())) {
                    String groupId = UUID.randomUUID().toString();
                    RestaurantMenuOptionGroup group = new RestaurantMenuOptionGroup();
                    group.setId(groupId);
                    group.setMenuId(menuId);
                    group.setName(g.getName());
                    group.setRequired(g.isRequired());
                    group.setMaxSelect(g.getMaxSelect());
                    group.setCreatedAt(now);
                    group.setUpdatedAt(now);
                    optionGroupDao.insert(conn, group);

                    for (OptionDto o : safe(g.getOptions())) {
                        RestaurantMenuOption opt = new RestaurantMenuOption();
                        opt.setId(UUID.randomUUID().toString());
                        opt.setOptionGroupId(groupId);
                        opt.setName(o.getName());
                        opt.setPrice(o.getPrice());
                        opt.setCreatedAt(now);
                        opt.setUpdatedAt(now);
                        optionDao.insert(conn, opt);
                    }
                }
            }

            return restaurantId;
        } catch (Exception e) {
            throw new RestaurantRegistrationException("Failed to register restaurant", e);
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    private <T> List<T> safe(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
