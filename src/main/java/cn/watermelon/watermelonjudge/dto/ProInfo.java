package cn.watermelon.watermelonjudge.dto;

import lombok.Data;

@Data
public class ProInfo {

    int problemId;

    int rankId;

    int status;

    int tries = 0;

    int time;

}
