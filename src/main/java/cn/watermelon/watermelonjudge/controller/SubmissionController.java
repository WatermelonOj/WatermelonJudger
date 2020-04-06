package cn.watermelon.watermelonjudge.controller;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.job.JudgeWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private JudgeWork judgeWork;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    Submission insertSubmission(Integer pid, Integer uid, String language, String code) {
        ProblemResult problemResult = new ProblemResult();
        problemResult.setSubTime(new Date());

        problemResult.setUserId(uid);
        problemResult.setProblemId(pid);
        problemResult.setLanguage(language);
        problemResult.setSourceCode(code);

        return judgeWork.judge(problemResult);
    }

}
