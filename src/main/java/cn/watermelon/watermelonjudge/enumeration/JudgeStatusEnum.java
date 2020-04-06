package cn.watermelon.watermelonjudge.enumeration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 判题状态类型
 * @author Acerkoo
 * @version 1.0.0
 */
@Slf4j
public enum JudgeStatusEnum {

    Compiling(0, "COMPILING"),

    Accept(1, "AC"),

    Compile_Error(2, "CE"),

    Presentation_Error(3, "PE"),

    Runtime_Error(4, "RE"),

    Time_limit_Exceeded(5, "TLE"),

    Memory_Limit_Exceeded(6, "MLE"),

    Wrong_Answer(7, "WA"),

    Queuing(8, "QUEUEING"),

    Judging(9, "Judging");

    private Integer status;

    private String desc;

    public Integer getStatus() { return status; }

    public String getDesc() { return desc; }

    JudgeStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static JudgeStatusEnum getStatusEnum(Integer status) {
        JudgeStatusEnum[] statusEnums = JudgeStatusEnum.values();
        for (JudgeStatusEnum statusEnum: statusEnums) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
