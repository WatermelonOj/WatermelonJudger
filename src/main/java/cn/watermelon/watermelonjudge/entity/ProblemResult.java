package cn.watermelon.watermelonjudge.entity;

import cn.watermelon.watermelonjudge.dto.TestResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 提交记录实体类
 * @author Acerkoo
 * @version 1.0.0
 */
@Data
public class ProblemResult implements Serializable {

    private static final long serivalVersionUID =  4807723399097914322L;

    private Integer subId;

    private Integer userId;

    private Integer problemId;

    private Integer contestId;

    private Integer status;

    private String language;

    private Long runTime;

    private Long runMemory;

    private String errorMsg;

    private String sourceCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date subTime;

    private ConcurrentSkipListMap<String, TestResult> resultMap = new ConcurrentSkipListMap<>();

}
