CREATE TABLE `contest_with_problem`
(
    `contest_id` INT,
    `problem_id` INT,
    `is_delete` BIT DEFAULT FALSE,
    PRIMARY KEY(contest_id,problem_id)
)
    COLLATE = utf8mb4_general_ci
    ENGINE = Innodb
    DEFAULT CHARSET = utf8mb4;