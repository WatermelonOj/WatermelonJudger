package cn.watermelon.watermelonjudge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserActivity {

    int acNum;

    int subNum;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    Date date;

}
