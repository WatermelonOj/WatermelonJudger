package cn.watermelon.watermelonjudge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserActivity {

    List<Integer> acNums;

    List<Integer> subNums;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    List<Date> dates;

}
