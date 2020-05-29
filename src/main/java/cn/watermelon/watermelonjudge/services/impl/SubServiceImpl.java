package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.*;
import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import cn.watermelon.watermelonjudge.enumeration.ProblemTag;
import cn.watermelon.watermelonjudge.enumeration.TagSort;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.mapper.UtilMapper;
import cn.watermelon.watermelonjudge.services.SubService;
import cn.watermelon.watermelonjudge.util.ConvertUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public List<JudgeCount> getUserJudgeResult(int userId) {
        List<JudgeCount> judgeCounts = new ArrayList<>();
        JudgeStatusEnum[] judgeStatusEnums = JudgeStatusEnum.values();
        for (JudgeStatusEnum judgeStatusEnum: judgeStatusEnums) {
            if (judgeStatusEnum.equals(JudgeStatusEnum.Compiling) || judgeStatusEnum.equals(JudgeStatusEnum.Queuing)
                    || judgeStatusEnum.equals(JudgeStatusEnum.Judging)) {
                continue;
            }
            String desc = judgeStatusEnum.getDesc();
            int status = judgeStatusEnum.getStatus();
            List<ProblemResult> prList = utilMapper.getUserStatusNumber(userId, status);
            JudgeCount judgeCount = new JudgeCount();
            judgeCount.setUserId(userId);
            judgeCount.setNum(prList.size());
            judgeCount.setResult(desc);
            judgeCounts.add(judgeCount);
        }
        return judgeCounts;
    }

    @Override
    public List<UserAbilitiy> getUserAbility(int userId) {
        List<UserAbilitiy> userAbilitiys = new ArrayList<>();
        TagSort[] tagSorts = TagSort.values();
        for (TagSort tagSort: tagSorts) {
            String desc = tagSort.getDesc();
            int type = tagSort.getType();
            UserAbilitiy userAbilitiy = new UserAbilitiy();
            userAbilitiy.setUserId(userId);
            userAbilitiy.setAbility(desc);
            userAbilitiy.setType(type);
            userAbilitiy.setNum(0);
            userAbilitiys.add(userAbilitiy);
        }

        List<ProblemResult> problemResultList = problemResultMapper.getAllAcSubmissionsByUser(userId);
        int all = problemResultList.size();

        int []list = new int[15];
        ProblemTag[] problemTags = ProblemTag.values();
        List<Integer> problemIds = utilMapper.getProblemList();
        for (Integer problemId: problemIds) {
            List<String> tags = utilMapper.getProblemTag(problemId);
            if (tags == null || tags.size() == 0) {
                tags.add(ProblemTag.SiWei.getDesc());
            }
            for (String tag: tags) {
                ProblemTag problemTag = ProblemTag.getProblemTag(tag);
                int type = problemTag.getType() + 1;
                list[type] += 1;
            }
        }


        for (ProblemResult problemResult: problemResultList) {
            int problemId = problemResult.getProblemId();
            List<String> tags = utilMapper.getProblemTag(problemId);
            if (tags == null || tags.size() == 0) {
                tags.add(ProblemTag.SiWei.getDesc());
            }
            for (String tag: tags) {
                ProblemTag problemTag = ProblemTag.getProblemTag(tag);
                userAbilitiys.get(problemTag.getType() + 1).addOne();
            }
        }

        int i = 0;
        for (UserAbilitiy userAbilitiy: userAbilitiys) {
            int now = userAbilitiy.getNum();
            if (Math.abs(now - list[i]) < 3) {
                list[i] += 4;
            }

            int level = (int) Math.sqrt(Math.sqrt(now / all) *  Math.sqrt(now / list[i])) * 6;
            if (level > 6) level = 6;
            if (level <= 1) level = 1;

            userAbilitiy.setLevel(level);
            i++;
        }

        return userAbilitiys;
    }

    @Override
    public List<UserLanguage> getUserLanguage(int userId) {
        List<UserLanguage> userLanguages = new ArrayList<>();
        LanguageEnum[] languageEnums = LanguageEnum.values();
        for (LanguageEnum languageEnum: languageEnums) {
            String language = languageEnum.getType();
            List<ProblemResult> problemResults = utilMapper.getUserLanguageNumber(userId, language);
            UserLanguage userLanguage = new UserLanguage();
            userLanguage.setUserId(userId);
            userLanguage.setLanguage(language);
            userLanguage.setNum(problemResults.size());
            userLanguages.add(userLanguage);
        }
        return userLanguages;
    }

    List<ProblemDTO> getProblemByTag(String tag) {
        List<Integer> problemIds = utilMapper.getProblemsByTag(tag);
        List<ProblemDTO> problemList = new ArrayList<>();
        for (Integer problemId: problemIds) {
            ProblemDTO problemDTO = utilMapper.getProblemByPID(problemId);
            problemDTO.setAcNum(utilMapper.getProblemAcNum(problemId));
            problemDTO.setSubNum(utilMapper.getProblemSubNum(problemId));
            problemList.add(problemDTO);
        }
        return problemList;
    }

    @Override
    public List<ProblemDTO> getUserProblem(int userId) {
        List<UserAbilitiy> userAbilitiys = getUserAbility(userId);
        List<ProblemDTO> problemDTOS = new ArrayList<>();
        if (problemResultMapper.getAllSubmissionsByUser(userId).size() < 5) {
            problemDTOS.addAll(utilMapper.getProblems());
        } else {

            userAbilitiys.sort(new Comparator<UserAbilitiy>() {
                @Override
                public int compare(UserAbilitiy o1, UserAbilitiy o2) {
                    if (o1.getLevel() < o2.getLevel()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            ProblemTag[] problemTags = ProblemTag.values();
            for (UserAbilitiy userAbilitiy: userAbilitiys) {
                int tag = userAbilitiy.getType();
                for (ProblemTag problemTag: problemTags) {
                    if (tag == problemTag.getType()) {
                        List<ProblemDTO> list = getProblemByTag(problemTag.getDesc());
                        if (list.size() == 0 && tag == -1) {
                            list.addAll(utilMapper.getProblems());
                        }
                        for (ProblemDTO problemDTO: list) {
                            if (problemDTOS.contains(problemDTO)) continue;
                            problemDTOS.add(problemDTO);
                        }
                    } else if (tag < problemTag.getType()) {
                        break;
                    }
                }
            }
        }

        problemDTOS.sort(new Comparator<ProblemDTO>() {
            @Override
            public int compare(ProblemDTO o1, ProblemDTO o2) {
                if (o1.getProblemId() < o2.getProblemId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        return problemDTOS.subList(0, 10);
    }

    @Override
    public UserActivity getUserActivity(int userId) {
        Calendar c = Calendar.getInstance();
        Date endDate = new Date();
        endDate.setHours(0);
        endDate.setMinutes(0);
        c.setTime(endDate);
        c.add(Calendar.DATE, -30);
        Date beginDate = c.getTime();

        UserActivity userActivity = new UserActivity();

        List<Integer> acNums = new ArrayList<>();
        List<Integer> subNums = new ArrayList<>();
        List<Date> dates = new ArrayList<>();

        while (true) {
            if (beginDate.equals(endDate)) {
                break;
            }
            c.setTime(beginDate);
            c.add(Calendar.DATE, 1);
            Date nxtDate = c.getTime();
            acNums.add(utilMapper.getAcPRByUID(userId, beginDate, nxtDate));
            subNums.add(utilMapper.getAllPRByUID(userId, beginDate, nxtDate));
            dates.add(beginDate);

            beginDate = nxtDate;
        }

        userActivity.setAcNums(acNums);
        userActivity.setSubNums(subNums);
        userActivity.setDates(dates);

        return userActivity;
    }

}
