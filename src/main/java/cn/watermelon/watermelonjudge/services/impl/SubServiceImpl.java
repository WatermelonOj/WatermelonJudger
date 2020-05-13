package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.mapper.UtilMapper;
import cn.watermelon.watermelonjudge.services.SubService;
import cn.watermelon.watermelonjudge.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServiceImpl implements SubService {

    @Autowired
    private UtilMapper utilMapper;

    @Autowired
    private ProblemResultMapper problemResultMapper;

    @Override
    public List<Submission> getGoodPr(int problemId) {
        return ConvertUtil.prs2Subs(utilMapper.getGoodPrByProblemId(problemId), problemResultMapper);
    }

}
