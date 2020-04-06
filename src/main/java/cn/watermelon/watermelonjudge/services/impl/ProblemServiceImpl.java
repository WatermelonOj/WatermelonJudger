package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.services.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 题目状态更新
 * @author Acerkoo
 * @version 1.0.0
 */
@Service
@Slf4j
public class ProblemServiceImpl implements ProblemService {

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

    /**
     * 更新题目状态
     * @return
     */
    @Override
    public void updateProblemResultStatusById(Integer problemResultId, Integer status) {
        problemResultMapper.updateProblemResultStatus(problemResultId, status);
    }

}
