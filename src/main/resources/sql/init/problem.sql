CREATE TABLE IF NOT EXISTS `problem`
(
    `problem_id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `time_limit` INT NOT NULL,
    `memeory_limit` INT NOT NULL
)

    COLLATE = utf8mb4_general_ci
    ENGINE = Innodb
    DEFAULT CHARSET = utf8mb4;