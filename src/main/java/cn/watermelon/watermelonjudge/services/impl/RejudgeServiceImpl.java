package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.mapper.RejudgeMapper;
import cn.watermelon.watermelonjudge.services.RejudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RejudgeServiceImpl implements RejudgeService {

    @Autowired
    private RejudgeMapper rejudgeMapper;

    @Override
    public List<ProblemResult> getRecordByProblemId(int problemId) {
        return rejudgeMapper.getProblemResultByPID(problemId);
    }

    @Override
    public void updateStatus(int problemId) {
        rejudgeMapper.updateStatus(problemId, JudgeStatusEnum.Compiling.getStatus());
    }
}
