package cn.watermelon.watermelonjudge.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProblemDTO {

    int problemId;

    String title;

    String description;

    String input;

    String output;

    String hint;

    boolean isSpj;

    int contestId;

    boolean visible;

    String tmLimit;

    String sampleInput;

    String sampleOutput;

    String memLimit;

    String status;

    int rankId;

    List<String> problemTags;

    int acNum;

    int subNum;

}
