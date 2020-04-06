package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.entity.ProblemResult;

/**
 * 用户代码编译与执行
 * @author Acerkoo
 * @version 1.0.0
 */
public interface JudgeService {

    /**
     * 编译
     * @param problemResult
     * @return
     */
    String compile(ProblemResult problemResult);

    /**
     * 执行
     * @param problemResult
     * @param userDirPath
     */
    void execute(ProblemResult problemResult, String userDirPath);

}
