package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;

import java.util.List;

public interface SubService {

    List<Submission> getGoodPr(int problemId);

}
