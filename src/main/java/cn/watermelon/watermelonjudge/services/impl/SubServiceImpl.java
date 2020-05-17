package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.*;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import cn.watermelon.watermelonjudge.enumeration.ProblemTag;
import cn.watermelon.watermelonjudge.enumeration.TagSort;
import cn.watermelon.watermelonjudge.mapper.ProblemResultMapper;
import cn.watermelon.watermelonjudge.mapper.UtilMapper;
import cn.watermelon.watermelonjudge.services.SubService;
import cn.watermelon.watermelonjudge.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
        ProblemTag[] problemTags = ProblemTag.values();
        for (ProblemTag problemTag: problemTags) {
            String tag = problemTag.getDesc();
            TagSort tagSort = TagSort.getTagSort(problemTag.getType());
            String desc = tagSort.getDesc();
            int type = tagSort.getType();
            List<ProblemResult> problemResultList = utilMapper.getUserAbility(userId, tag);
            if (problemTag.getType() == -1) {
                problemResultList.addAll(utilMapper.getUserAbility(userId, null));
            }
            UserAbilitiy userAbilitiy = new UserAbilitiy();
            userAbilitiy.setUserId(userId);
            userAbilitiy.setAbility(desc);
            userAbilitiy.setType(type);

            int all = problemResultMapper.getAllSubmissionsByUser(userId).size();
            int now = problemResultList.size();

            int level = (int) Math.sqrt(Math.sqrt(now / all) * 6 + 1);
            if (level > 6) level = 6;
            if (level <= 0) level = 0;

            userAbilitiy.setLevel(level);
            userAbilitiys.add(userAbilitiy);
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
        if (userAbilitiys == null || problemResultMapper.getAllSubmissionsByUser(userId).size() < 5) {
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

        return problemDTOS;
    }

}
