CREATE TABLE `user` (
                        `id` CHAR(36) NOT NULL,
                        `birth` VARCHAR(255) NULL,
                        `email` VARCHAR(255) NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `city_code` (
                             `id` CHAR(36) NOT NULL,
                             `city_code` VARCHAR(255) NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `district_code` (
                                 `id` CHAR(36) NOT NULL,
                                 `city_code_id` CHAR(36) NOT NULL,
                                 `district` VARCHAR(255) NULL,
                                 PRIMARY KEY (`id`),
                                 CONSTRAINT `fk_district_city`
                                     FOREIGN KEY (`city_code_id`) REFERENCES `city_code`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 매장 / 룸 / 타임슬롯 -----------------------------------------------

CREATE TABLE `restaurant` (
                              `id` CHAR(36) NOT NULL,
                              `user_id` CHAR(36) NOT NULL,
                              `Field` FLOAT NULL,
                              `cnt_review` INT NULL,
                              `Field2` VARCHAR(255) NULL,
                              `city_code_id` CHAR(36) NOT NULL,
                              PRIMARY KEY (`id`),
                              CONSTRAINT `fk_restaurant_user`
                                  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                              CONSTRAINT `fk_restaurant_city`
                                  FOREIGN KEY (`city_code_id`) REFERENCES `city_code`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 매장 룸(테이블)
CREATE TABLE `restaurant_room` (
                                   `id` CHAR(36) NOT NULL,
                                   `restaurant_id` CHAR(36) NOT NULL,
                                   `name` VARCHAR(255) NULL,
                                   `capacity` INT NOT NULL,
                                   `room_type` VARCHAR(255) NULL,
                                   `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
                                   `description` VARCHAR(255) NULL,
                                   PRIMARY KEY (`id`),
                                   CONSTRAINT `fk_room_restaurant`
                                       FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `restaurant_timeslot_rule` (
                                            `id` CHAR(36) NOT NULL,
                                            `day_of_week` TINYINT NULL,
                                            `open_time` TIME NULL,
                                            `close_time` TIME NULL,
                                            `slot_interval` INT NULL,
                                            `usage_time` INT NULL,
                                            `team_capacity` INT NULL,   -- 동시 수용 팀/룸 개수
                                            `allow_waiting` BOOLEAN NULL,
                                            `restaurant_id` CHAR(36) NOT NULL,
                                            `is_holiday` BOOLEAN NULL,
                                            PRIMARY KEY (`id`),
                                            CONSTRAINT `fk_timeslot_rule_restaurant`
                                                FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `timeslot_instance` (
                                     `id` VARCHAR(255) NOT NULL,
                                     `timeslot_rule_id` CHAR(36) NOT NULL,
                                     `date` DATE NULL,
                                     `start_time` TIME NULL,
                                     `team_capacity` INT NULL,   -- 최대 팀 수
                                     `reserved_team` INT NULL,   -- 예약된 팀 수
                                     `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
                                     PRIMARY KEY (`id`),
                                     CONSTRAINT `fk_timeslot_instance_rule`
                                         FOREIGN KEY (`timeslot_rule_id`) REFERENCES `restaurant_timeslot_rule`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 예약 및 룸 매핑 -----------------------------------------------------

CREATE TABLE `reservation` (
                               `id` CHAR(36) NOT NULL,
                               `restaurant_id` CHAR(36) NOT NULL,
                               `user_id` CHAR(36) NOT NULL,
                               `cost` VARCHAR(255) NULL,
                               `state` VARCHAR(255) NULL,
                               `persons` VARCHAR(255) NULL,
                               `date` VARCHAR(255) NULL,
                               `start_time` VARCHAR(255) NULL,
                               `end_time` VARCHAR(255) NULL,
                               `usage_time` VARCHAR(255) NULL,
                               PRIMARY KEY (`id`),
                               CONSTRAINT `fk_reservation_restaurant`
                                   FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                               CONSTRAINT `fk_reservation_user`
                                   FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `reservation_room` (
                                    `id` CHAR(36) NOT NULL,
                                    `reservation_id` CHAR(36) NOT NULL,
                                    `room_id` CHAR(36) NOT NULL,
                                    `persons` INT NULL,
                                    PRIMARY KEY (`id`),
                                    CONSTRAINT `fk_reservation_room_reservation`
                                        FOREIGN KEY (`reservation_id`) REFERENCES `reservation`(`id`),
                                    CONSTRAINT `fk_reservation_room_room`
                                        FOREIGN KEY (`room_id`) REFERENCES `restaurant_room`(`id`),
                                    CONSTRAINT `uq_reservation_room` UNIQUE (`reservation_id`, `room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `reservation_slot` (
                                    `id` VARCHAR(255) NOT NULL,
                                    `restaurant_id` CHAR(36) NOT NULL,
                                    `Field` VARCHAR(255) NULL,
                                    PRIMARY KEY (`id`),
                                    CONSTRAINT `fk_reservation_slot_restaurant`
                                        FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `reservation_service` (
                                       `id` CHAR(36) NOT NULL,
                                       `reservation_id` CHAR(36) NOT NULL,
                                       `Key` VARCHAR(255) NOT NULL,
                                       `service_id` VARCHAR(255) NULL,
                                       `cost` VARCHAR(255) NULL,
                                       `description` VARCHAR(255) NULL,
                                       `service_cnt` INT NULL,
                                       PRIMARY KEY (`id`),
                                       CONSTRAINT `fk_reservation_service_reservation`
                                           FOREIGN KEY (`reservation_id`) REFERENCES `reservation`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 과금 ---------------------------------------------------------------

CREATE TABLE `monthly_charge` (
                                  `id` VARCHAR(255) NOT NULL,
                                  `restaurant_id` CHAR(36) NOT NULL,
                                  `charge_month` CHAR(7) NOT NULL,          -- 'YYYY-MM'
                                  `total_ad_cnt_per_month` INT NULL,
                                  `total_usage_cnt_per_month` INT NULL,
                                  `total_charge` FLOAT NULL,
                                  `charge_per_reservation` FLOAT NULL,
                                  `status` VARCHAR(255) NULL,
                                  PRIMARY KEY (`id`),
                                  CONSTRAINT `fk_monthly_charge_restaurant`
                                      FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                                  UNIQUE KEY `ux_monthly_charge_restaurant_month` (`restaurant_id`, `charge_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `is_charged` (
                              `id` VARCHAR(255) NOT NULL,
                              `monthly_charge_id` CHAR(36) NOT NULL,
                              `is_paid` BOOLEAN NULL,
                              `paid_time` DATETIME NULL,
                              `total_charge` FLOAT NULL,
                              PRIMARY KEY (`id`),
                              CONSTRAINT `fk_is_charged_monthly_charge`
                                  FOREIGN KEY (`monthly_charge_id`) REFERENCES `monthly_charge`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 매장/유저 관계 ------------------------------------------------------

CREATE TABLE `user_recent_view` (
                                    `id` VARCHAR(255) NOT NULL,
                                    `restaurant_id` CHAR(36) NOT NULL,
                                    `user_id` CHAR(36) NOT NULL,
                                    PRIMARY KEY (`id`),
                                    CONSTRAINT `fk_recent_view_restaurant`
                                        FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                                    CONSTRAINT `fk_recent_view_user`
                                        FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `favorites` (
                             `id` VARCHAR(255) NOT NULL,
                             `user_id` CHAR(36) NOT NULL,
                             `restaurant_id` CHAR(36) NOT NULL,
                             PRIMARY KEY (`id`),
                             CONSTRAINT `fk_favorites_user`
                                 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                             CONSTRAINT `fk_favorites_restaurant`
                                 FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_restaurant_ban` (
                                       `Key` VARCHAR(255) NOT NULL,
                                       `user_id` CHAR(36) NOT NULL,
                                       `restaurant_id` CHAR(36) NOT NULL,
                                       PRIMARY KEY (`Key`),
                                       CONSTRAINT `fk_ban_user`
                                           FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                       CONSTRAINT `fk_ban_restaurant`
                                           FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_report_restaurant` (
                                          `id` VARCHAR(255) NOT NULL,
                                          `user_id` CHAR(36) NOT NULL,
                                          `restaurant_id` CHAR(36) NOT NULL,
                                          PRIMARY KEY (`id`),
                                          CONSTRAINT `fk_user_report_restaurant_user`
                                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                          CONSTRAINT `fk_user_report_restaurant_restaurant`
                                              FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `restaurant_report_user` (
                                          `Key` VARCHAR(255) NOT NULL,
                                          `restaurant_id` CHAR(36) NOT NULL,
                                          `user_id` CHAR(36) NOT NULL,
                                          PRIMARY KEY (`Key`),
                                          CONSTRAINT `fk_restaurant_report_user_restaurant`
                                              FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                                          CONSTRAINT `fk_restaurant_report_user_user`
                                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 키워드 / 사진 / 공지 ----------------------------------------------

CREATE TABLE `restaurant_keyword` (
                                      `id` VARCHAR(255) NOT NULL,
                                      `keyword` VARCHAR(255) NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `restaurant_keyword_mapping_table` (
                                                    `id` CHAR(36) NOT NULL,
                                                    `keyword_id` VARCHAR(255) NOT NULL,
                                                    `restaurant_id` CHAR(36) NOT NULL,
                                                    `user_recent_view_id` CHAR(36) NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    CONSTRAINT `fk_kw_mapping_keyword`
                                                        FOREIGN KEY (`keyword_id`) REFERENCES `restaurant_keyword`(`id`),
                                                    CONSTRAINT `fk_kw_mapping_restaurant`
                                                        FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                                                    CONSTRAINT `fk_kw_mapping_recent_view`
                                                        FOREIGN KEY (`user_recent_view_id`) REFERENCES `user_recent_view`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `restaurant_picture` (
                                      `id` CHAR(36) NOT NULL,
                                      `restaurant_picture` VARCHAR(255) NULL,
                                      `restaurant_id` CHAR(36) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      CONSTRAINT `fk_restaurant_picture_restaurant`
                                          FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `notice` (
                          `id` CHAR(36) NOT NULL,
                          `restaurant_id` CHAR(36) NOT NULL,
                          `user_id` CHAR(36) NOT NULL,
                          `text` VARCHAR(255) NULL,
                          PRIMARY KEY (`id`),
                          CONSTRAINT `fk_notice_restaurant`
                              FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                          CONSTRAINT `fk_notice_user`
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 리뷰 / 좋아요 / 신고 / 대댓글 -------------------------------------

CREATE TABLE `review` (
                          `id` CHAR(36) NOT NULL,
                          `user_id` CHAR(36) NOT NULL,
                          `reservation_id` CHAR(36) NOT NULL,
                          `review_point` VARCHAR(255) NULL,
                          `review_content` VARCHAR(200) NULL,
                          `state` VARCHAR(255) NULL,
                          `embedding_vector` VARCHAR(255) NULL,
                          PRIMARY KEY (`id`),
                          CONSTRAINT `fk_review_user`
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                          CONSTRAINT `fk_review_reservation`
                              FOREIGN KEY (`reservation_id`) REFERENCES `reservation`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `rereview` (
                            `id` CHAR(36) NOT NULL,
                            `review_id` CHAR(36) NOT NULL,
                            `user_id` CHAR(36) NOT NULL,
                            `review_content` VARCHAR(255) NULL,
                            PRIMARY KEY (`id`),
                            CONSTRAINT `fk_rereview_review`
                                FOREIGN KEY (`review_id`) REFERENCES `review`(`id`),
                            CONSTRAINT `fk_rereview_user`
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `review_picture` (
                                  `id` CHAR(36) NOT NULL,
                                  `review_id` CHAR(36) NOT NULL,
                                  `Key` VARCHAR(255) NOT NULL,
                                  `review_picture` VARCHAR(255) NULL,
                                  PRIMARY KEY (`id`),
                                  CONSTRAINT `fk_review_picture_review`
                                      FOREIGN KEY (`review_id`) REFERENCES `review`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `review_report` (
                                 `id` CHAR(36) NOT NULL,
                                 `review_id` CHAR(36) NOT NULL,
                                 `user_id` CHAR(36) NOT NULL,
                                 `TimeStamp` VARCHAR(255) NULL,
                                 `content` VARCHAR(255) NULL,
                                 PRIMARY KEY (`id`),
                                 CONSTRAINT `fk_review_report_review`
                                     FOREIGN KEY (`review_id`) REFERENCES `review`(`id`),
                                 CONSTRAINT `fk_review_report_user`
                                     FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `review_like` (
                               `id` CHAR(36) NOT NULL,
                               `user_id` CHAR(36) NOT NULL,
                               `review_id` CHAR(36) NOT NULL,
                               PRIMARY KEY (`id`),
                               CONSTRAINT `fk_review_like_user`
                                   FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                               CONSTRAINT `fk_review_like_review`
                                   FOREIGN KEY (`review_id`) REFERENCES `review`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_report_review` (
                                      `id` VARCHAR(255) NOT NULL,
                                      `user_id` CHAR(36) NOT NULL,
                                      `review_id` CHAR(36) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      CONSTRAINT `fk_user_report_review_user`
                                          FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                      CONSTRAINT `fk_user_report_review_review`
                                          FOREIGN KEY (`review_id`) REFERENCES `review`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 유저 부가 정보 / 쿠폰 / 웨이팅 ------------------------------------

CREATE TABLE `user_picture` (
                                `id` CHAR(36) NOT NULL,
                                `user_picture` VARCHAR(255) NULL,
                                `user_id` CHAR(36) NOT NULL,
                                PRIMARY KEY (`id`),
                                CONSTRAINT `fk_user_picture_user`
                                    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `coupon` (
                          `id` VARCHAR(255) NOT NULL,
                          `user_id` CHAR(36) NOT NULL,
                          `sale` INT NULL,
                          PRIMARY KEY (`id`),
                          CONSTRAINT `fk_coupon_user`
                              FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `waiting` (
                           `id` VARCHAR(255) NOT NULL,
                           `restaurant_id` CHAR(36) NOT NULL,
                           `user_id` CHAR(36) NOT NULL,
                           `party_size` INT NULL,
                           `usage_per_team` INT NULL,
                           `waiting_start_time` DATETIME NULL,
                           `expected_start_time` DATETIME NULL,
                           `expected_waiting_minute` INT NULL,
                           `order` INT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `fk_waiting_restaurant`
                               FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant`(`id`),
                           CONSTRAINT `fk_waiting_user`
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
