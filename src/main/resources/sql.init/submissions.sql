CREATE TABLE IF NOT EXISTS `submissions`
(
    `sub_id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `problem_id` INT NOT NULL,
    `contest_id` INT,
    `user_id` INT NOT NULL,
    `sub_time` TIMESTAMP NOT NULL COMMENT '提交时间',
    `run_time` INT,
    `run_memory` INT,
    `result` INT,
    `code` TEXT,
    `language` VARCHAR(20),
    `is_delete` BIT
)

    COLLATE = utf8mb4_general_ci
    ENGINE = Innodb
    DEFAULT CHARSET = utf8mb4;