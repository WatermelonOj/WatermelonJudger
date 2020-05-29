package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.job.JudgeWork;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.mapper.RejudgeMapper;
import cn.watermelon.watermelonjudge.services.RecordService;
import cn.watermelon.watermelonjudge.services.RejudgeService;
import cn.watermelon.watermelonjudge.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class RejudgeServiceImpl implements RejudgeService {

    @Autowired
    private RejudgeMapper rejudgeMapper;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ProblemResultMapper problemResultMapper;

    @Autowired
    private JudgeWork judgeWork;

    @Value("/home/admin/problem/")
//    @Value("C:\\Users\\74798\\Desktop\\problem\\")
    private String fileServiceSaveDir;

    @Override
    public List<ProblemResult> getRecordByProblemId(int problemId) {
        return rejudgeMapper.getProblemResultByPID(problemId);
    }

    @Override
    public void updateStatus(int problemId) {
        rejudgeMapper.updateStatus(problemId, JudgeStatusEnum.Compiling.getStatus());
    }

    @Override
    public boolean addProblemTest(int problemId, String input, String output, int userId) {
        try {

            File fileDir = new File(fileServiceSaveDir + problemId + "/input/");
            int num = fileDir.listFiles().length + 1;
            String goalInputOneDir = fileServiceSaveDir + problemId + "/input/" + num + ".txt";
            String goalOutputOneDir = fileServiceSaveDir + problemId + "/output/" + num + ".txt";

            FileUtil.saveFile(input, goalInputOneDir);
            FileUtil.saveFile(output, goalOutputOneDir);

            ProblemResult problemResult = problemResultMapper.getProAcSubmissionsByUser(problemId, userId);
            problemResult.setRejudge(true);
            Submission submission = judgeWork.judge(problemResult, false);

            if (submission.getResult() != JudgeStatusEnum.Accept) {
                return false;
            }

            judgeWork.rejudge(problemId);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
