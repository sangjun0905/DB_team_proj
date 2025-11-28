DROP TRIGGER IF EXISTS trg_reservation_after_insert_evaluate_charge$$
CREATE TRIGGER trg_reservation_after_insert_evaluate_charge
    AFTER INSERT ON reservation
    FOR EACH ROW
    trg: BEGIN
    DECLARE v_date   DATE;
    DECLARE v_month  CHAR(7);
    DECLARE v_id     VARCHAR(255);
    DECLARE v_price  FLOAT;

    DECLARE CONTINUE HANDLER FOR NOT FOUND
        SET v_id = NULL, v_price = NULL;

    IF NEW.state IS NULL OR NEW.state <> 'RESERVED' THEN
        LEAVE trg;
END IF;

    SET v_date = STR_TO_DATE(NEW.date, '%Y-%m-%d');
    IF v_date IS NULL THEN
        LEAVE trg;
END IF;

    SET v_month = DATE_FORMAT(v_date, '%Y-%m');

SELECT id, charge_per_reservation
INTO v_id, v_price
FROM monthly_charge
WHERE restaurant_id = NEW.restaurant_id
  AND charge_month  = v_month
    LIMIT 1
     FOR UPDATE;

IF v_id IS NULL THEN
        SET v_price = 1000.00;
        SET v_id   = UUID();

INSERT INTO monthly_charge(
    id,
    restaurant_id,
    charge_month,
    total_ad_cnt_per_month,
    total_usage_cnt_per_month,
    total_charge,
    charge_per_reservation,
    status
)
VALUES (
           v_id,
           NEW.restaurant_id,
           v_month,
           0,
           0,
           0,
           v_price,
           'ACTIVE'
       );
END IF;

UPDATE monthly_charge
SET total_usage_cnt_per_month = COALESCE(total_usage_cnt_per_month, 0) + 1,
    total_charge              = COALESCE(total_charge, 0) + charge_per_reservation
WHERE id = v_id;
END$$

DELIMITER ;