package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Acerkoo
 * @version 1.0.0
 */
@Service
public interface RecordService {

    Problem getProblemById(Integer problemId);

    void updateProblemResultStatusById(Integer problemResultId, Integer status);

    void updateProblemResult(ProblemResult problemResult);

    Integer insertProblemRusult(ProblemResult problemResult);

    int getSubmissionsNum(int pageSize);

    List<ProblemResult> getSubmissions(int page, int pageSize);

    List<ProblemResult> getSubmissionsByUser(int userId, int page, int pageSize);

    List<ProblemResult> getSubmissionsByProblem(int problemId, int page, int pageSize);

    List<ProblemResult> getSubmissionsByContest(int contestId, int page, int pageSize);

    List<ProblemResult> getSubmissionsByStatus(String status, int page, int pageSize);

    List<ProblemResult> getSubmissionsByLanguage(String language, int page, int pageSize);

}
