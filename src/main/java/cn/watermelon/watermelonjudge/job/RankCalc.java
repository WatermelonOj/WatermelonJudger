package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.ProInfo;
import cn.watermelon.watermelonjudge.dto.Rank;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class RankCalc {

    @Autowired
    private ProblemResultMapper problemResultMapper;

    public List<Rank> getRankList(int contestId) {

        List<ProblemResult> prList = problemResultMapper.getSubmissionsByContestId(contestId);

        List<Rank> rankList = new ArrayList<>();

        Map<Integer, Integer> user = new HashMap<>();

        for (ProblemResult problemResult : prList) {

            int userId = problemResult.getUserId();
            int problemId = problemResult.getProblemId();
            int status = problemResult.getStatus();

            if (!user.containsKey(userId)) {
                Rank rank = new Rank();
                rank.setUserId(userId);
                rank.setUsername(problemResultMapper.getUsername(userId));
                user.put(userId, rankList.size());
                rank.setPenalty(0);
                rank.setSolveNum(0);
                rank.setProblem(new ArrayList<>());
                rankList.add(rank);
            }

            Rank rank = rankList.get(user.get(userId));

            List<ProInfo> proInfos = rank.getProblem();
            ProInfo proInfo = null;

            for (ProInfo proInfoo : proInfos) {
                if (problemId == proInfoo.getProblemId()) {
                    proInfo = proInfoo;
                    break;
                }
            }

            if (proInfo == null) {
                proInfo = new ProInfo();
                proInfo.setProblemId(problemId);
                proInfo.setStatus(0);
                proInfo.setTime(0);
                proInfo.setTries(0);
                proInfos.add(proInfo);
            }

            if (proInfo.getStatus() != 1) {
                if (status == 1) {
                    Date pass = problemResult.getSubTime();
                    Date begin = problemResultMapper.getContestStartTime(contestId);
                    int time = (int) ((pass.getTime() - begin.getTime()) / (1000 * 60));
                    proInfo.setTime(time);
                    rank.setPenalty(20 * proInfo.getTries() + time + rank.getPenalty());
                    rank.setSolveNum(rank.getSolveNum() + 1);
                    proInfo.setStatus(1);
                } else {
                    proInfo.setStatus(0);
                }
                proInfo.setTries(proInfo.getTries() + 1);
            }

        }

        Collections.sort(rankList);
        int rankId = 0;
        for (Rank rank : rankList) {
            rankId = rankId + 1;
            rank.setRankId(rankId);
        }
        return rankList;
    }

}
