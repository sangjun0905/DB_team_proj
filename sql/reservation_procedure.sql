DELIMITER $$

/* ---------- 2-1. 함수들 ---------- */

DROP FUNCTION IF EXISTS fn_is_valid_user$$
CREATE FUNCTION fn_is_valid_user(p_user_id CHAR(36))
    RETURNS TINYINT
    READS SQL DATA
BEGIN
    DECLARE v_cnt INT;

SELECT COUNT(*) INTO v_cnt
FROM `user`
WHERE id = p_user_id;

RETURN v_cnt > 0;
END$$

DROP FUNCTION IF EXISTS fn_is_user_banned$$
CREATE FUNCTION fn_is_user_banned(
    p_user_id       CHAR(36),
    p_restaurant_id CHAR(36)
)
    RETURNS TINYINT
    READS SQL DATA
BEGIN
    DECLARE v_cnt INT;

SELECT COUNT(*) INTO v_cnt
FROM user_restaurant_ban
WHERE user_id       = p_user_id
  AND restaurant_id = p_restaurant_id;

RETURN v_cnt > 0;
END$$

DROP FUNCTION IF EXISTS fn_is_open_time$$
CREATE FUNCTION fn_is_open_time(
    p_restaurant_id CHAR(36),
    p_date          DATE,
    p_start_time    TIME
)
    RETURNS TINYINT
    READS SQL DATA
BEGIN
    DECLARE v_cnt INT;
    DECLARE v_dow TINYINT;

    SET v_dow = DAYOFWEEK(p_date);  -- 1(일) ~ 7(토)

SELECT COUNT(*) INTO v_cnt
FROM restaurant_timeslot_rule
WHERE restaurant_id = p_restaurant_id
  AND day_of_week   = v_dow
  AND (is_holiday IS NULL OR is_holiday = FALSE)
  AND open_time  <= p_start_time
  AND close_time >  p_start_time;

RETURN v_cnt > 0;
END$$

DROP FUNCTION IF EXISTS fn_timeslot_remaining_capacity$$
CREATE FUNCTION fn_timeslot_remaining_capacity(
    p_timeslot_instance_id VARCHAR(255)
)
    RETURNS INT
    READS SQL DATA
BEGIN
    DECLARE v_cap INT;
    DECLARE v_res INT;

SELECT team_capacity,
       COALESCE(reserved_team, 0)
INTO v_cap, v_res
FROM timeslot_instance
WHERE id        = p_timeslot_instance_id
  AND is_active = TRUE;

IF v_cap IS NULL THEN
        RETURN 0;
END IF;

RETURN v_cap - v_res;
END$$

/* ---------- 2-2. 타임슬롯 생성 프로시저 ---------- */

DROP PROCEDURE IF EXISTS sp_generate_timeslots_for_date$$
CREATE PROCEDURE sp_generate_timeslots_for_date (
    IN p_restaurant_id CHAR(36),
    IN p_date          DATE
)
    proc: BEGIN
    DECLARE v_rule_id       CHAR(36);
    DECLARE v_day_of_week   TINYINT;
    DECLARE v_open_time     TIME;
    DECLARE v_close_time    TIME;
    DECLARE v_slot_interval INT;
    DECLARE v_team_capacity INT;
    DECLARE v_is_holiday    TINYINT;
    DECLARE v_current_time  TIME;

    SET v_day_of_week = DAYOFWEEK(p_date);

SELECT id,
       open_time,
       close_time,
       slot_interval,
       team_capacity,
       IFNULL(is_holiday, 0)
INTO v_rule_id,
    v_open_time,
    v_close_time,
    v_slot_interval,
    v_team_capacity,
    v_is_holiday
FROM restaurant_timeslot_rule
WHERE restaurant_id = p_restaurant_id
  AND day_of_week   = v_day_of_week
    LIMIT 1;

IF v_rule_id IS NULL OR v_is_holiday = 1 THEN
        LEAVE proc;
END IF;

DELETE FROM timeslot_instance
WHERE timeslot_rule_id = v_rule_id
  AND date             = p_date;

SET v_current_time = v_open_time;

    WHILE v_current_time < v_close_time DO
        IF fn_is_open_time(p_restaurant_id, p_date, v_current_time) = 1 THEN
            INSERT INTO timeslot_instance(
                id,
                timeslot_rule_id,
                date,
                start_time,
                team_capacity,
                reserved_team,
                is_active
            )
            VALUES (
                UUID(),
                v_rule_id,
                p_date,
                v_current_time,
                v_team_capacity,
                0,
                TRUE
            );
END IF;

        SET v_current_time =
            ADDTIME(v_current_time, SEC_TO_TIME(v_slot_interval * 60));
END WHILE;
END$$

DROP PROCEDURE IF EXISTS sp_generate_timeslots_for_range$$
CREATE PROCEDURE sp_generate_timeslots_for_range (
    IN p_restaurant_id CHAR(36),
    IN p_from_date     DATE,
    IN p_to_date       DATE
)
BEGIN
    DECLARE v_d DATE;

    IF p_from_date IS NULL OR p_to_date IS NULL
       OR p_from_date > p_to_date THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_DATE_RANGE_FOR_GENERATE';
END IF;

    SET v_d = p_from_date;

    WHILE v_d <= p_to_date DO
        CALL sp_generate_tsp_generate_timeslots_for_dateimeslots_for_date(p_restaurant_id, v_d);
        SET v_d = DATE_ADD(v_d, INTERVAL 1 DAY);
END WHILE;
END$$

/* ---------- 2-3. 예약 관련 P1~P6 ---------- */

-- P1) 기본 검증
DROP PROCEDURE IF EXISTS sp_reservation_validate_basic$$
CREATE PROCEDURE sp_reservation_validate_basic (
    IN p_restaurant_id CHAR(36),
    IN p_user_id       CHAR(36),
    IN p_date          DATE,
    IN p_start_time    TIME,
    IN p_persons       INT
)
BEGIN
    IF fn_is_valid_user(p_user_id) = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_USER';
END IF;

    IF fn_is_user_banned(p_user_id, p_restaurant_id) = 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'USER_BANNED';
END IF;

    IF p_date < CURDATE()
       OR p_date > DATE_ADD(CURDsp_generate_timeslots_for_datesp_generate_timeslots_for_rangesp_timeslot_increase_usageATE(), INTERVAL 30 DAY) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_DATE_RANGE';
END IF;

    IF p_persons IS NULL OR p_persons <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'INVALID_PERSONS';
END IF;
END$$

-- P2) sp_timeslot_increase_usagesp_generate_timeslots_for_rangesp_reservation_insert_mainsp_generate_timeslots_for_rangesp_generate_timeslots_for_rangesp_generate_timeslots_for_range타임슬롯 찾고 잠금
DROP PROCEDURE IF EXISTS sp_reservation_lock_timeslot$$
CREATE PROCEDURE sp_reservation_lock_timeslot (
    IN  p_restaurant_id CHAR(36),
    IN  p_date          DATE,
    IN  p_start_time    TIME,
    OUT o_timeslot_id   VARCHAR(255),
    OUT o_cap_teams     INT,
    OUT o_res_teams     INT
)
BEGIN
    SET o_timeslot_id = NULL;
    SET o_cap_teams   = NULL;
    SET o_res_teams   = NULL;

SELECT ti.id,
       ti.team_capacity,
       COALESCE(ti.reserved_team, 0)
INTO o_timeslot_id, o_cap_teams, o_res_teams
FROM timeslot_instance ti
         JOIN restaurant_timeslot_rule rtr
              ON ti.timeslot_rule_id = rtr.id
WHERE rtr.restaurant_id = p_restaurant_id
  AND ti.date           = p_date
  AND ti.start_time     = p_start_time
  AND ti.is_active      = TRUE
    LIMIT 1
     FOR UPDATE;

IF o_timeslot_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'TIMESLOT_NOT_FOUND_OR_INACTIVE';
END IF;

    IF (o_cap_teams - o_res_teams) < 1 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'NO_TEAM_CAPACITY';
END IF;
END$$

-- P3) 룸 선택 (tmp_selected_rooms에 저장)
DROP PROCEDURE IF EXISTS sp_reservation_pick_rooms$$
CREATE PROCEDURE sp_reservation_pick_rooms (
    IN p_restaurant_id CHAR(36),
    IN p_date          DATE,
    IN p_start_time    TIME,
    IN p_persons       INT
)
BEGIN
    DECLARE v_room_id        CHAR(36);
    DECLARE v_room_capacity  INT;
    DECLARE v_needed         INT;
    DECLARE v_done           TINYINT DEFAULT 0;

    DECLARE cur_rooms CURSOR FOR
SELECT rr.id, rr.capacity
FROM restaurant_room rr
WHERE rr.restaurant_id = p_restaurant_id
  AND rr.is_active     = TRUE
  AND rr.id NOT IN (
    SELECT rr2.room_id
    FROM reservation r2
             JOIN reservation_room rr2 ON r2.id = rr2.reservation_id
    WHERE r2.restaurant_id = p_restaurant_id
      AND r2.date       = DATE_FORMAT(p_date, '%Y-%m-%d')
      AND r2.start_time = DATE_FORMAT(p_start_time, '%H:%i:%s')
      AND r2.state IN ('RESERVED','CONFIRMED')
)
ORDER BY rr.capacity ASC;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

    CREATE TEMPORARY TABLE IF NOT EXISTS tmp_selected_rooms (
        room_id  CHAR(36) PRIMARY KEY,
        capacity INT NOT NULL
    );

DELETE FROM tmp_selected_rooms;

SET v_needed = p_persons;

OPEN cur_rooms;
rooms_loop: LOOP
        FETCH cur_rooms INTO v_room_id, v_room_capacity;
        IF v_done = 1 THEN
            LEAVE rooms_loop;
END IF;

INSERT INTO tmp_selected_rooms(room_id, capacity)
VALUES (v_room_id, v_room_capacity);

SET v_needed = v_needed - v_room_capacity;

        IF v_needed <= 0 THEN
            LEAVE rooms_loop;
END IF;
END LOOP;
CLOSE cur_rooms;

IF v_needed > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'NO_ROOM_CAPACITY';
END IF;
END$$

-- P4) reservation 메인 1행 INSERT
DROP PROCEDURE IF EXISTS sp_reservation_insert_main$$
CREATE PROCEDURE sp_reservation_insert_main (
    IN  p_restaurant_id  CHAR(36),
    IN  p_user_id        CHAR(36),
    IN  p_date           DATE,
    IN  p_start_time     TIME,
    IN  p_persons        INT,
    OUT o_reservation_id CHAR(36)
)
BEGIN
    DECLARE v_date_str VARCHAR(10);
    DECLARE v_time_str VARCHAR(8);

    SET o_reservation_id = UUID();
    SET v_date_str = DATE_FORMAT(p_date, '%Y-%m-%d');
    SET v_time_str = DATE_FORMAT(p_start_time, '%H:%i:%s');

INSERT INTO reservation
(id, restaurant_id, user_id,
 cost, state,
 persons, date, start_time, end_time, usage_time)
VALUES
    (o_reservation_id, p_restaurant_id, p_user_id,
     NULL, 'RESERVED',
     CAST(p_persons AS CHAR),
     v_date_str, v_time_str, NULL, NULL);
END$$

-- P5) tmp_selected_rooms → reservation_room
DROP PROCEDURE IF EXISTS sp_reservation_insert_rooms$$
CREATE PROCEDURE sp_reservation_insert_rooms (
    IN p_reservation_id CHAR(36)
)
BEGIN
INSERT INTO reservation_room (id, reservation_id, room_id, persons)
SELECT UUID(), p_reservation_id, room_id, NULL
FROM tmp_selected_rooms;
END$$

-- P6) 타임슬롯 예약 팀 수 +1
DROP PROCEDURE IF EXISTS sp_timeslot_increase_usage$$
CREATE PROCEDURE sp_timeslot_increase_usage (
    IN p_timeslot_id VARCHAR(255)
)
BEGIN
UPDATE timeslot_instance
SET reserved_team = COALESCE(reserved_team, 0) + 1
WHERE id = p_timeslot_id;
END$$

/* ---------- 2-4. 오케스트라 프로시저 ---------- */

DROP PROCEDURE IF EXISTS sp_create_reservation$$
CREATE PROCEDURE sp_create_reservation (
    IN p_restaurant_id CHAR(36),
    IN p_user_id       CHAR(36),
    IN p_date          DATE,
    IN p_start_time    TIME,
    IN p_persons       INT
)
BEGIN
    DECLARE v_ts_id           VARCHAR(255);
    DECLARE v_cap_teams       INT;
    DECLARE v_res_teams       INT;
    DECLARE v_reservation_id  CHAR(36);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
ROLLBACK;
DROP TEMPORARY TABLE IF EXISTS tmp_selected_rooms;
        RESIGNAL;
END;

START TRANSACTION;

CALL sp_reservation_validate_basic(
        p_restaurant_id,
        p_user_id,
        p_date,
        p_start_time,
        p_persons
    );

CALL sp_reservation_lock_timeslot(
        p_restaurant_id,
        p_date,
        p_start_time,
        v_ts_id,
        v_cap_teams,
        v_res_teams
    );

CALL sp_reservation_pick_rooms(
        p_restaurant_id,
        p_date,
        p_start_time,
        p_persons
    );

CALL sp_reservation_insert_main(
        p_restaurant_id,
        p_user_id,
        p_date,
        p_start_time,
        p_persons,
        v_reservation_id
    );

CALL sp_reservation_insert_rooms(v_reservation_id);

CALL sp_timeslot_increase_usage(v_ts_id);

COMMIT;

DROP TEMPORARY TABLE IF EXISTS tmp_selected_rooms;
END$$