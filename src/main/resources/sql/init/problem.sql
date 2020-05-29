CREATE TABLE IF NOT EXISTS `problem`(
    `problem_id` INT PRIMARY KEY ,
    `title` VARCHAR(255) ,
    `description` TEXT,
    `input` TEXT,
    `output` TEXT,
    `sample_input` TEXT,
    `sample_output` TEXT,
    `hint` TEXT,
    `spj` BIT,
    `contest_id` VARCHAR(255),
    `visible` BIT,
    `tm_limit` VARCHAR(255),
    `mem_limit` VARCHAR(255)
)

    COLLATE = utf8mb4_general_ci
    ENGINE = Innodb
    DEFAULT CHARSET = utf8mb4;

INSERT INTO `problem` (`problem_id`, `tm_limit`, `mem_limit`) VALUES (100, 1000, 1000)
INSERT INTO `problem` VALUES (1000, 'A + B Problem', '���� $a$ �� $b$�����  �Ľ����', 'һ������������ $a$ �� $b$��', 'һ��һ�������� $a+b$��', '1 2', '3', null, false, null, true, 1000, 1000);


