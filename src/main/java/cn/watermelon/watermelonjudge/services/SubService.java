package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.dto.*;

import java.util.List;

public interface SubService {

    List<Submission> getGoodPr(int problemId);

    List<JudgeCount> getUserJudgeResult(int userId);

    List<UserAbilitiy> getUserAbility(int userId);

    List<UserLanguage>  getUserLanguage(int userId);

    List<ProblemDTO> getUserProblem(int userId);

    List<UserActivity> getUserActivity(int userId);

}
