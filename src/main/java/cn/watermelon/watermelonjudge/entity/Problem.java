package cn.watermelon.watermelonjudge.entity;

import lombok.Data;

/**
 * 问题实体类
 * @author Acerkoo
 * @version 1.0.0
 */
@Data
public class Problem {

    /**
     * 题目 ID
     */
    private Integer problemId;

    private String tmLimit;

    private String memLimit;

    /**
     * 时间限制
     */
    private Long timeLimit;

    /**
     * 空间限制
     */
    private Long memoryLimit;

//    /**
//     * 是否 spj
//     */
//    private Integer flag;

}
