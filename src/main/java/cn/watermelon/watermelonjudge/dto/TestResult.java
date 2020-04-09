package cn.watermelon.watermelonjudge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 判题中间记录
 * @author Acerkoo
 * @version 1.0.0
 **/
@Data
public class TestResult {

    private Integer id;

    private Integer proReId;

    private String num;

    private String userOutput;

    private Long time;

    private Long memory;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateTime;

}
