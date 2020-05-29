package cn.watermelon.watermelonjudge.dto;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * 用户提交记录的信息记录 dto
 * @author Acerkoo
 * @version 1.0.0
 */
@Data
public class Submission {

    /**
     * key 值
     */
    private Integer subId;

    /**
     * 对应题目 id
     */
    private Integer problemId;

    /**
     * 对应比赛 id
     */
    private Integer contestId;

    /**
     * 提交用户 id
     */
    private Integer userId;

    /**
     * 代码提交时间
     * 格式为 yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date subTime;

    /**
     * 用户代码运行时间
     */
    private Long runTime;

    /**
     * 用户代码消耗内存
     */
    private Long runMemory;

    /**
     * 用户代码内容
     */
    private String code;

    /**
     * 错误时，用户代码返回信息
     */
    private String msg;

    /**
     * 判题状态
     */
    private JudgeStatusEnum result;

    /**
     * 代码语言
     */
    private LanguageEnum language;

    private String username;

    public Submission(ProblemResult problemResult) {
        this.subId = problemResult.getSubId();
        this.problemId = problemResult.getProblemId();
        this.contestId = problemResult.getContestId();
        this.userId = problemResult.getUserId();
        this.subTime = problemResult.getSubTime();
        this.runTime = problemResult.getRunTime();
        this.runMemory = problemResult.getRunMemory();
        this.code = problemResult.getSourceCode();
        this.msg = problemResult.getErrorMsg();
        this.result = JudgeStatusEnum.getStatusEnum(problemResult.getStatus());
        this.language = LanguageEnum.getEnumByType(problemResult.getLanguage());
    }

}
