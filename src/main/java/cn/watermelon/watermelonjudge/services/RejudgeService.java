package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.entity.ProblemResult;

import java.util.List;

public interface RejudgeService {

    List<ProblemResult> getRecordByProblemId(int problemId);

    void updateStatus(int problemId);

    boolean addProblemTest(int problemId, String input, String output, int userId);

}
