package cn.watermelon.watermelonjudge.dto;

import com.alibaba.fastjson.annotation.JSONField;
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

    private Integer num;

    private String userOutput;

    private Long time;

    private Long memory;

    private Integer status;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
