package cn.watermelon.watermelonjudge.controller;

import cn.watermelon.watermelonjudge.dto.Rank;
import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.job.JudgeWork;
import cn.watermelon.watermelonjudge.job.RankCalc;
import cn.watermelon.watermelonjudge.services.RecordService;
import cn.watermelon.watermelonjudge.services.RejudgeService;
import cn.watermelon.watermelonjudge.services.SubService;
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

    @Autowired
    private RankCalc rankCalc;

    @Autowired
    private SubService subService;

    @Autowired
    private RejudgeService rejudgeService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    Submission insertSubmission(Integer pid, Integer uid, String language, String code, Integer contestId) {

        ProblemResult problemResult = new ProblemResult();
        problemResult.setSubTime(new Date());
        problemResult.setContestId(contestId);
        problemResult.setUserId(uid);
        problemResult.setProblemId(pid);
        problemResult.setLanguage(language);
        problemResult.setSourceCode(code);
        problemResult.setRejudge(false);

        return judgeWork.judge(problemResult, false);
    }

    @RequestMapping(value = "/rejudge", method = RequestMethod.PATCH)
    void rejudgeProblem(Integer problemId) {
        judgeWork.rejudge(problemId);
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
        return recordService.getSubmissions(page, pageSize);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    List<Submission> getSubmissionsByUser(int userId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return recordService.getSubmissionsByUser(userId, page, pageSize);
    }

    @RequestMapping(value = "/problem", method = RequestMethod.GET)
    List<Submission> getSubmissionsByProblem(int problemId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return recordService.getSubmissionsByProblem(problemId, page, pageSize);
    }

    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    List<Rank> getContestRank(int contestId){
        return rankCalc.getRankList(contestId);
    }

    @RequestMapping(value = "/contest", method = RequestMethod.GET)
    List<Submission> getSubmissionsByContest(int contestId, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return recordService.getSubmissionsByContest(contestId, page, pageSize);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    List<Submission> getSubmissionsByStatus(String status, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return recordService.getSubmissionsByStatus(status, page, pageSize);
    }

    @RequestMapping(value = "/language", method = RequestMethod.GET)
    List<Submission> getSubmissionsByLanguage(String language, Integer page, Integer pageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        }
        if (page == null) {
            page = 1;
        }
        return recordService.getSubmissionsByLanguage(language, page, pageSize);
    }

    @RequestMapping(value = "/good", method = RequestMethod.GET)
    List<Submission> getGoodSubmission(int problemId) {
        List<Submission> result = subService.getGoodPr(problemId);
        return result.subList(0, Math.min(10, result.size()));
    }

    @RequestMapping(value = "/hack", method = RequestMethod.POST)
    boolean problemHack(int problemId, String input, String output, int userId) {
        return rejudgeService.addProblemTest(problemId, input, output, userId);
    }

}
