package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.services.RecordService;
import cn.watermelon.watermelonjudge.util.ConvertUtil;
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
    public List<Submission> getSubmissions(int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissions(begin, pageSize), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByUser(int userId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByUser(userId, begin, pageSize), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByProblem(int problemId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByProblem(problemId, begin, pageSize), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByContest(int contestId) {
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByContestId(contestId), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByContest(int contestId, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByContest(contestId, begin, pageSize), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByStatus(String qstatus, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        int status = 0;
        JudgeStatusEnum[] judgeStatusEnums = JudgeStatusEnum.values();
        for (JudgeStatusEnum judgeStatusEnum : judgeStatusEnums) {
            if (judgeStatusEnum.getDesc().equals(qstatus)) {
                status = judgeStatusEnum.getStatus();
                break;
            }
        }
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByStatus(status, begin, pageSize), problemResultMapper);
    }

    @Override
    public List<Submission> getSubmissionsByLanguage(String language, int page, int pageSize) {
        int begin = (page - 1) * pageSize;
        return ConvertUtil.prs2Subs(problemResultMapper.getSubmissionsByLanguage(language, begin, pageSize), problemResultMapper);
    }

}
