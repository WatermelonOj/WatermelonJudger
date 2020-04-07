package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.services.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 题目状态更新
 * @author Acerkoo
 * @version 1.0.0
 */
@Service
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Autowired
    private ProblemResultMapper problemResultMapper;

    @Override
    public Problem getProblemById(Integer problemId) {
        return problemResultMapper.getProblemById(problemId);
    }

    /**
     * 更新题目结果
     * @return
     */
    @Override
    public void updateProblemResult(ProblemResult problemResult) {
        problemResultMapper.updateByPrimaryKeySelective(problemResult);
    }

    @Override
    public Integer insertProblemRusult(ProblemResult problemResult) {
        problemResultMapper.insertSubmission(problemResult);
        return problemResultMapper.getProblemResultId();
    }

    @Override
    public int getSubmissionsNum(int pageSize) {
        int num = problemResultMapper.getSubmissionsNumber();
        return (num + pageSize - 1) / pageSize;
    }

    /**
     * 更新题目状态
     * @return
     */
    @Override
    public void updateProblemResultStatusById(Integer problemResultId, Integer status) {
        problemResultMapper.updateProblemResultStatus(problemResultId, status);
    }

    @Override
    public List<ProblemResult> getSubmissions(int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return problemResultMapper.getSubmissions(begin, pageSize);
    }

    @Override
    public List<ProblemResult> getSubmissionsByUser(int userId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return problemResultMapper.getSubmissionsByUser(userId, begin, pageSize);
    }

    @Override
    public List<ProblemResult> getSubmissionsByProblem(int problemId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return problemResultMapper.getSubmissionsByProblem(problemId, begin, pageSize);
    }

    @Override
    public List<ProblemResult> getSubmissionsByContest(int contestId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return problemResultMapper.getSubmissionsByContest(contestId, begin, pageSize);
    }

    @Override
    public List<ProblemResult> getSubmissionsByStatus(String qstatus, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        int status = 0;
        JudgeStatusEnum[] judgeStatusEnums = JudgeStatusEnum.values();
        for (JudgeStatusEnum judgeStatusEnum : judgeStatusEnums) {
            if (judgeStatusEnum.getDesc().equals(qstatus)) {
                status = judgeStatusEnum.getStatus();
                break;
            }
        }
        return problemResultMapper.getSubmissionsByStatus(status, begin, pageSize);
    }

    @Override
    public List<ProblemResult> getSubmissionsByLanguage(String language, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return problemResultMapper.getSubmissionsByLanguage(language, begin, pageSize);
    }

}
