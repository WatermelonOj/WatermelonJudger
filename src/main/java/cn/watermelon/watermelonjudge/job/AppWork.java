package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.Submission;
import cn.watermelon.watermelonjudge.services.RecordService;
import cn.watermelon.watermelonjudge.services.impl.RecordServiceImpl;

import java.util.*;

public class AppWork {

    double[][] buyer = new double[205][1205];

    double[][] sim = new double[1205][1205];

    AppRecord[] problem = new AppRecord[1205];

    public List<Integer> getProblem(int userId) {

        this.getBuyer(userId);

        this.getSim();

        this.getRec(userId);

        Arrays.sort(problem, new AppRecord.MyComparator());
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (problem[i].rec > 0) {
                res.add(problem[i].problemId);
            }
        }
        return res;
    }

    private void getBuyer(int userId) {
        List<Submission> sub = new ArrayList<>();//数据库获取所有提交记录
        RecordService recordService = new RecordServiceImpl();
        sub = recordService.getAllSubmissionsByUser(userId);
        Iterator<Submission> it = sub.iterator();
        while (it.hasNext()) {
            Submission item = (Submission) it.next();
            if (buyer[item.getUserId()][item.getProblemId()] > 0) {
                continue;
            }
            Date now = new Date();
            long nowTime = now.getTime();
            long submitTime = item.getSubTime().getTime();
            long days = daysDifference(submitTime, nowTime);
            if (days >= 30) break;
            buyer[item.getUserId()][item.getProblemId()] = Math.pow(Math.E, -0.25 * days);
        }
    }

    private void getSim() {
        for (int i = 1; i <= 1200; i++) {
            for (int j = 1; j < i; j++) {
                double up = 0;
                double downi = 0;
                double downj = 0;
                for (int k = 1; k <= 200; k++) {
                    up = up + buyer[k][i] * buyer[k][j];
                    downi = downi + buyer[k][i] * buyer[k][i];
                    downj = downj + buyer[k][j] * buyer[k][j];
                }
                double down = Math.sqrt(downi) * Math.sqrt(downj);
                if (down > 0) sim[i][j] = sim[j][i] = up / down;
            }
        }
    }

    private void getRec(int userId) {
        for (int i = 1; i < 1200; i++) {
            problem[i].rec = 0;
            problem[i].problemId = i;
            for (int j = 1; j <= 1200; j++) {
                problem[i].rec += buyer[userId][j] * sim[j][i];
            }
        }
    }

    private long daysDifference(long start, long end) {
        long between = end - start;
        long day = between / (24 * 60 * 60 * 1000);
        return day;
    }
}
