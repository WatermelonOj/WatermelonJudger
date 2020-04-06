package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;

/**
 * @author Acerkoo
 * @version 1.0.0
 */
public interface ProblemService {

    Problem getProblemById(Integer problemId);

    void updateProblemResultStatusById(Integer problemResultId, Integer status);

    void updateProblemResult(ProblemResult problemResult);

     Integer insertProblemRusult(ProblemResult problemResult);

}
