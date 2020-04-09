package cn.watermelon.watermelonjudge.controller;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.job.JudgeWork;
import cn.watermelon.watermelonjudge.services.RecordService;
import cn.watermelon.watermelonjudge.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    private final Integer defaultPageSize = 50;

    @Autowired
    private JudgeWork judgeWork;

    @Autowired
    private RecordService recordService;

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

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    int getSubmissionPages(Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        return recordService.getSubmissionsNum(pageSize);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    List<Submission> getSubmissions(Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissions(page, pageSize));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    List<Submission> getSubmissionsByUser(int userId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissionsByUser(userId, page, pageSize));
    }

    @RequestMapping(value = "/problem", method = RequestMethod.GET)
    List<Submission> getSubmissionsByProblem(int problemId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissionsByProblem(problemId, page, pageSize));
    }

    @RequestMapping(value = "/contest", method = RequestMethod.GET)
    List<Submission> getSubmissionsByContest(int contestId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissionsByContest(contestId, page, pageSize));
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    List<Submission> getSubmissionsByStatus(String status, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissionsByStatus(status, page, pageSize));
    }

    @RequestMapping(value = "/language", method = RequestMethod.GET)
    List<Submission> getSubmissionsByLanguage(String language, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return ConvertUtil.prs2Subs(recordService.getSubmissionsByLanguage(language, page, pageSize));
    }

}
