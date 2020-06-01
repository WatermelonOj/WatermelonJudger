package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.handler.BaseHandler;
import cn.watermelon.watermelonjudge.services.JudgeService;
import cn.watermelon.watermelonjudge.services.RejudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.List;

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

    @Autowired
    private RejudgeService rejudgeService;

    public Submission judge(ProblemResult problemResult, Boolean rejudge) {

        // 编译
        String userDirPath = judgeService.compile(problemResult, rejudge);

        // 运行
        if (userDirPath != null) {
            BaseHandler.sendMessageToAllUsers(new TextMessage("submission"));
            judgeService.execute(problemResult, userDirPath);
        }

        Submission submission = new Submission(problemResult);

        if (rejudge != true) {

        } else {

        }

        BaseHandler.sendMessageToAllUsers(new TextMessage("submission"));
        if (problemResult.getContestId() != null) {
            BaseHandler.sendMessageToAllUsers(new TextMessage("rank"));
        }
        return submission;
    }

    public void rejudge(Integer problemId) {

        rejudgeService.updateStatus(problemId);

        List<ProblemResult> problemResults = rejudgeService.getRecordByProblemId(problemId);

        for (ProblemResult problemResult: problemResults) {
            judge(problemResult, true);
        }

    }
}
