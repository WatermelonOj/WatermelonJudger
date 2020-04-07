package cn.watermelon.watermelonjudge.util;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

    public static List<Submission> prs2Subs(List<ProblemResult> list) {
        List<Submission> result = new ArrayList<>();
        for (ProblemResult problemResult: list) {
            result.add(new Submission(problemResult));
        }
        return result;
    }

}
