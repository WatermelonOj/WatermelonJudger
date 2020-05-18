package cn.watermelon.watermelonjudge.controller;

import cn.watermelon.watermelonjudge.dto.*;
import cn.watermelon.watermelonjudge.services.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class AbilityController {

    @Autowired
    private SubService subService;

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    List<JudgeCount> getUserJudgeResult(int userId) {
        return subService.getUserJudgeResult(userId);
    }

    @RequestMapping(value = "/ability", method = RequestMethod.GET)
    List<UserAbilitiy> getUserAbility(int userId) {
        return subService.getUserAbility(userId);
    }

    @RequestMapping(value = "/language", method = RequestMethod.GET)
    List<UserLanguage> getUserLanguage(int userId) {
        return subService.getUserLanguage(userId);
    }

    @RequestMapping(value = "/problem", method = RequestMethod.GET)
    List<ProblemDTO> getUserProblem(int userId) {
        return subService.getUserProblem(userId);
    }

    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    List<UserActivity> getUserActivity(int userId) {
        return subService.getUserActivity(userId);
    }

}
