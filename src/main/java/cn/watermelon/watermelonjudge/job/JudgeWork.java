package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.services.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消费者, 处理消息队列中的提交记录
 * @author Acerkoo
 * @version 1.0.0
 */
@Component
@Slf4j
public class JudgeWork {

    @Autowired
    private JudgeService judgeService;

    public Submission judge(ProblemResult problemResult) {

        // 编译
        String userDirPath = judgeService.compile(problemResult);

        // 运行
        if (userDirPath != null) {
            judgeService.execute(problemResult, userDirPath);
        }

        return new Submission(problemResult);
    }
}
