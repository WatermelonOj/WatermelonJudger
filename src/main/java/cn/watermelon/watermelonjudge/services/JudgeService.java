package cn.watermelon.watermelonjudge.services;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.springframework.stereotype.Service;

/**
 * 用户代码编译与执行
 * @author Acerkoo
 * @version 1.0.0
 */
@Service
public interface JudgeService {

    /**
     * 编译
     * @param problemResult
     * @return
     */
    String compile(ProblemResult problemResult, Boolean rejudge);

    /**
     * 执行
     * @param problemResult
     * @param userDirPath
     */
    void execute(ProblemResult problemResult, String userDirPath);

}
