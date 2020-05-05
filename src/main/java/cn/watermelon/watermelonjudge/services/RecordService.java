package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.dto.Submission;
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

    List<Submission> getSubmissions(int page, int pageSize);

    List<Submission> getSubmissionsByUser(int userId, int page, int pageSize);

    List<Submission> getSubmissionsByProblem(int problemId, int page, int pageSize);

    List<Submission> getSubmissionsByContest(int contestId);

    List<Submission> getSubmissionsByContest(int contestId, int page, int pageSize);

    List<Submission> getSubmissionsByStatus(String status, int page, int pageSize);

    List<Submission> getSubmissionsByLanguage(String language, int page, int pageSize);

}
