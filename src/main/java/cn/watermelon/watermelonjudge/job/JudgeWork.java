package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
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
        BaseHandler.sendMessageToAllUsers(new TextMessage("submission"));

        if (userDirPath == null) {
            System.out.println("Complice Twice");
            userDirPath = judgeService.compile(problemResult, true);
        }

        if (userDirPath == null) {
            System.out.println("Complice Three");
        }

        if (userDirPath != null) {
            System.out.println("userDirPath = " + userDirPath);
        } else {
            System.out.println("userDirPath is null");
        }
        // 运行
        judgeService.execute(problemResult, userDirPath);
        BaseHandler.sendMessageToAllUsers(new TextMessage("submission"));

        Submission submission = new Submission(problemResult);

        if (rejudge != true) {

        } else {

        }

        System.out.println("~~~~~JudgeWork~~~63 ~~~contestId = " + problemResult.getContestId());
        if (problemResult.getContestId() != null && problemResult.getContestId() > 0) {
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
        Exce

    }
}
