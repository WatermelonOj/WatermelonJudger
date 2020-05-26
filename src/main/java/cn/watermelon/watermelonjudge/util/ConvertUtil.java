package cn.watermelon.watermelonjudge.util;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    public static List<Submission> prs2Subs(List<ProblemResult> list, ProblemResultMapper problemResultMapper) {
        List<Submission> result = new ArrayList<>();
        for (ProblemResult problemResult: list) {
            Submission submission = new Submission(problemResult);
            result.add(submission);
        }
        return result;
    }

}
