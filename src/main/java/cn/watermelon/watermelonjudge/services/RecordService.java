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

    String getUserLastSubmission(int problemId, int userId);

    void updateProblemResultStatusById(Integer problemResultId, Integer status);

    void updateProblemResult(ProblemResult problemResult);

    Integer insertProblemRusult(ProblemResult problemResult);

    int getUserAcNum(int userId);

    int getUserSubNum(int userId);

    int getSubmissionsNum(int pageSize);

    List<Submission> getAllSubmissionsByUser(int userId);

    List<Submission> getSubmissions(int page, int pageSize);

    List<Submission> getSubmissionsByUser(int userId, int page, int pageSize);

    List<Submission> getSubmissionsByProblem(int problemId, int page, int pageSize);

    List<Submission> getSubmissionsByContest(int contestId);

    List<Submission> getSubmissionsByContest(int contestId, int page, int pageSize);

    List<Submission> getSubmissionsByStatus(String status, int page, int pageSize);

    List<Submission> getSubmissionsByLanguage(String language, int page, int pageSize);

}
