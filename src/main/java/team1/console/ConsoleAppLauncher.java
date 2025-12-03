package team1.console;

import team1.config.DbConfig;
import team1.dao.RestaurantDao;
import team1.dao.jdbc.*;
import team1.dto.booking.BookingResultDto;
import team1.dto.booking.CreateReservationRequestDto;
import team1.dto.booking.CreateWaitingRequestDto;
import team1.dto.booking.UpdateBookingStateRequestDto;
import team1.dto.restaurant.*;
import team1.facade.BookingManagementFacade;
import team1.service.booking.BookingService;
import team1.service.restaurant.RestaurantRegistrationException;
import team1.service.restaurant.RestaurantRegistrationService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleAppLauncher {

    public static void main(String[] args) throws Exception {
        DbConfig db = DbConfig.load();
        DataSource dataSource = new SimpleDriverDataSource(db.getUrl(), db.getUser(), db.getPass());

        // DAO/Service/Facade 구성
        RestaurantDao restaurantDao = new RestaurantDao();
        RestaurantRegistrationService registrationService = new RestaurantRegistrationService(
                dataSource,
                restaurantDao,
                new JdbcRestaurantUserRoleDao(),
                new JdbcRestaurantRoomDao(),
                new JdbcRestaurantTimeslotRuleDao(),
                new JdbcRestaurantBreakTimeDao(),
                new JdbcRestaurantSpecialDayDao(),
                new JdbcRestaurantMenuDao(),
                new JdbcRestaurantMenuOptionGroupDao(),
                new JdbcRestaurantMenuOptionDao()
        );
        BookingService bookingService = new BookingService(dataSource);
        BookingManagementFacade facade = new BookingManagementFacade(bookingService, registrationService);
        runMenu(facade, restaurantDao);
    }

    private static void runMenu(BookingManagementFacade facade, RestaurantDao restaurantDao) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("==== Console Menu ====");
            System.out.println("1) Register sample restaurant");
            System.out.println("2) List timeslots");
            System.out.println("3) Create reservation");
            System.out.println("4) Create waiting");
            System.out.println("5) Update booking state");
            System.out.println("6) Exit");
            System.out.print("Select: ");
            String choice;
            try {
                choice = sc.nextLine();
            } catch (Exception eof) {
                System.out.println("Input stream closed. Exiting.");
                break;
            }
            try {
                switch (choice) {
                    case "1" -> registerSampleRestaurant(facade);
                    case "2" -> queryTimeslots(sc, restaurantDao);
                    case "3" -> createReservation(sc, facade);
                    case "4" -> createWaiting(sc, facade);
                    case "5" -> updateBookingState(sc, facade);
                    case "6" -> running = false;
                    default -> System.out.println("잘못된 선택");
                }
            } catch (Exception e) {
                System.out.println("오류: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    private static void registerSampleRestaurant(BookingManagementFacade facade) {
        RestaurantRegistrationRequest req = new RestaurantRegistrationRequest();
        RestaurantBasicDto basic = new RestaurantBasicDto();
        basic.setOwnerUserId("owner-uuid");
        basic.setName("샘플식당");
        basic.setCityId("city-uuid");
        basic.setDistrictId("district-uuid");
        basic.setAddress("서울시 어딘가 123");
        basic.setPhone("010-0000-0000");
        basic.setSupportsReservation(true);
        basic.setSupportsWaiting(true);
        req.setRestaurant(basic);

        // 룸
        RoomDto room = new RoomDto();
        room.setName("홀1");
        room.setCapacity(20);
        room.setRoomType("HALL");
        req.setRooms(List.of(room));

        // 타임슬롯 룰
        TimeslotRuleDto rule = new TimeslotRuleDto();
        rule.setDayOfWeek(1);
        rule.setOpenTime(LocalTime.of(9, 0));
        rule.setCloseTime(LocalTime.of(21, 0));
        rule.setSlotInterval(30);
        rule.setUsageTime(60);
        rule.setTeamCapacity(10);
        rule.setHoliday(false);
        rule.setAllowWaiting(true);
        req.setTimeslotRules(List.of(rule));

        // 브레이크타임
        BreakTimeDto bt = new BreakTimeDto();
        bt.setDayOfWeek(1);
        bt.setBreakStartTime(LocalTime.of(15, 0));
        bt.setBreakEndTime(LocalTime.of(17, 0));
        bt.setMemo("브레이크");
        req.setBreakTimes(List.of(bt));

        // 스페셜데이
        SpecialDayDto sd = new SpecialDayDto();
        sd.setDate(LocalDate.now().plusDays(7));
        sd.setType("HOLIDAY");
        sd.setAllowReservation(false);
        sd.setAllowWaiting(false);
        sd.setMemo("정기휴무");
        req.setSpecialDays(List.of(sd));

        // 메뉴
        OptionDto opt = new OptionDto();
        opt.setName("곱빼기");
        opt.setPrice(2000);
        OptionGroupDto og = new OptionGroupDto();
        og.setName("추가옵션");
        og.setRequired(false);
        og.setMaxSelect(2);
        og.setOptions(List.of(opt));
        MenuDto menu = new MenuDto();
        menu.setName("비빔밥");
        menu.setPrice(8000);
        menu.setDescription("맛있는 비빔밥");
        menu.setSortOrder(1);
        menu.setActive(true);
        menu.setOptionGroups(List.of(og));
        req.setMenus(List.of(menu));

        try {
            String restaurantId = facade.registerRestaurantForOwner(req);
            System.out.println("샘플 식당 등록 완료. ID=" + restaurantId);
        } catch (Exception e) {
            System.out.println("식당 등록 실패: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void queryTimeslots(Scanner sc, RestaurantDao restaurantDao) throws SQLException {
        System.out.print("restaurantId: ");
        String restId = sc.nextLine();
        System.out.print("date (yyyy-MM-dd): ");
        String dateStr = sc.nextLine();
        LocalDate date = LocalDate.parse(dateStr);
        restaurantDao.findTimeslots(restId, date).forEach(ts -> {
            System.out.println(ts.getId() + " | " + ts.getDate() + " " + ts.getStartTime() +
                    " cap=" + ts.getTeamCapacity() + " reserved=" + ts.getReservedTeam());
        });
    }

    private static void createReservation(Scanner sc, BookingManagementFacade facade) throws Exception {
        CreateReservationRequestDto req = new CreateReservationRequestDto();
        System.out.print("userId: ");
        String userId = sc.nextLine();
        req.setUserId(userId);
        System.out.print("restaurantId: ");
        req.setRestaurantId(sc.nextLine());
        System.out.print("timeslotInstanceId: ");
        req.setTimeslotInstanceId(sc.nextLine());
        System.out.print("persons: ");
        req.setPersons(Integer.parseInt(sc.nextLine()));
        System.out.print("couponId(optional): ");
        String coupon = sc.nextLine();
        req.setCouponId(coupon.isBlank() ? null : coupon);

        BookingResultDto result = facade.createReservationForUser(userId, req);
        System.out.println("예약 생성 완료: " + result.getBookingId() + " state=" + result.getState());
    }

    private static void createWaiting(Scanner sc, BookingManagementFacade facade) throws Exception {
        CreateWaitingRequestDto req = new CreateWaitingRequestDto();
        System.out.print("userId: ");
        String userId = sc.nextLine();
        req.setUserId(userId);
        System.out.print("restaurantId: ");
        req.setRestaurantId(sc.nextLine());
        System.out.print("persons: ");
        req.setPersons(Integer.parseInt(sc.nextLine()));
        req.setWaitingStartTime(LocalDateTime.now());

        BookingResultDto result = facade.createWaitingForUser(userId, req);
        System.out.println("웨이팅 생성 완료: " + result.getBookingId() + " state=" + result.getState());
    }

    private static void updateBookingState(Scanner sc, BookingManagementFacade facade) throws Exception {
        UpdateBookingStateRequestDto req = new UpdateBookingStateRequestDto();
        System.out.print("ownerUserId: ");
        String owner = sc.nextLine();
        System.out.print("bookingId: ");
        req.setBookingId(sc.nextLine());
        System.out.print("newState: ");
        req.setNewState(sc.nextLine());
        System.out.print("description: ");
        req.setDescription(sc.nextLine());

        facade.changeBookingStateByOwner(owner, req);
        System.out.println("상태 변경 완료");
    }
}
