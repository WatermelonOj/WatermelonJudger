CREATE TABLE `contest`
(
    `contest_id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(255),
    `description` TEXT,
    `hostname` VARCHAR(255),
    `create_time` TIMESTAMP,
    `start_time` TIMESTAMP NULL,
    `end_time` TIMESTAMP NULL,
    `is_delete` BIT DEFAULT FALSE
)
    COLLATE = utf8mb4_general_ci
    ENGINE = Innodb
    DEFAULT CHARSET = utf8mb4;
