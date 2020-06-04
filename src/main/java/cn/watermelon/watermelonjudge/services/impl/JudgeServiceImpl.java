package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.TestResult;
import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import cn.watermelon.watermelonjudge.handler.BaseHandler;
import cn.watermelon.watermelonjudge.job.TestInputWork;
import cn.watermelon.watermelonjudge.services.*;
import cn.watermelon.watermelonjudge.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户代码编译与执行
 *
 * @author Acerkoo
 * @version 1.0.0
 */
@Slf4j
@Service
public class JudgeServiceImpl implements JudgeService {

    private static final String envOs = CmdUtil.envOs;

    @Value("/home/admin/problem")
//    @Value("/Users/szki/Desktop/problem")
    private String fileServerTestcaseDir;

    private static Runtime runtime = Runtime.getRuntime();

    @Autowired
    private RecordService recordService;

    /**
     * 返回用户代码保存目录
     * @param problemResult
     * @return 用户代码保存目录
     */
    @Override
    public String compile(ProblemResult problemResult, Boolean rejudge) {
        String problemDirpath = fileServerTestcaseDir + envOs + problemResult.getProblemId();
        String userDirPath = problemDirpath + envOs + "submission" + envOs + UUIDUtil.createByTime();
        LanguageEnum languageEnum = LanguageEnum.getEnumByType(problemResult.getLanguage());
        String ext = languageEnum.getExt();
        FileUtil.saveFile(problemResult.getSourceCode(), userDirPath + envOs + "Main." + ext);

        String compileErrorOutput = null;

        if (!rejudge) {
            problemResult.setStatus(JudgeStatusEnum.Compiling.getStatus());
            int subId = recordService.insertProblemRusult(problemResult);
            System.out.println("~~~~~judgeImpl " + 69 + "~~~~~~~" + JudgeStatusEnum.getStatusEnum(problemResult.getStatus()).getDesc());
            BaseHandler.sendMessageToAllUsers(new TextMessage("submission"));
            problemResult.setSubId(subId);
        }

        String cmd = null;
        if (languageEnum.isRequiredCompile()) {
            try {
                cmd = CmdUtil.compileCmd(problemResult.getLanguage(), userDirPath);
                Process process = runtime.exec(cmd);
                compileErrorOutput = StreamUtil.getOutput(process.getErrorStream());
            } catch (IOException e) {
                e.printStackTrace();
                String message = "".equals(e.getMessage()) ? "IOException" : e.getMessage();
                log.info(message);
                compileErrorOutput = message;
            }
        }

        System.out.println("compileErrorOutput = " + compileErrorOutput);
        System.out.println("cmd = " + cmd);

        if (compileErrorOutput == null || "".equals(compileErrorOutput) || cmd != null) {

            try {
                String execPath = userDirPath + envOs;
                if (LanguageEnum.Java8.getType().equals(problemResult.getLanguage())) {
                    execPath += "Main.java";
                } else if (LanguageEnum.C.getType().equals(problemResult.getLanguage())){
                    execPath += "C.out";
                } else if (LanguageEnum.CPP.getType().equals(problemResult.getLanguage())){
                    execPath += "C++.out";
                }
                File outExecFile = new File(execPath);
                if (!outExecFile.exists()) {
                    throw new FileNotFoundException();
                }
            } catch (Exception e) {
                problemResult.setStatus(JudgeStatusEnum.Compile_Error.getStatus());
                System.out.println("~~~~~judgeImpl " + 104 + "~~~~~~~" + JudgeStatusEnum.getStatusEnum(problemResult.getStatus()).getDesc());
                recordService.updateProblemResultStatusById(problemResult.getSubId(), problemResult.getStatus());
                userDirPath = null;
            }

            problemResult.setErrorMsg(compileErrorOutput);
            return userDirPath;
        } else {
            // update compile error
            compileErrorOutput = StringUtil.getLimitLenghtByString(compileErrorOutput, 10000);
            problemResult.setStatus(JudgeStatusEnum.Compile_Error.getStatus());
            System.out.println("~~~~~judgeImpl " + 114 + "~~~~~~~" + JudgeStatusEnum.Compile_Error.getDesc());
            problemResult.setErrorMsg(compileErrorOutput);
            problemResult.setRunMemory(0L);
            problemResult.setRunTime(0L);
            recordService.updateProblemResult(problemResult);
            return null;
        }
    }

    /**
     * 获取用户代码执行结果
     *
     * @param problemResult 提交记录
     * @param userDirPath   用户代码保存目录
     */
    @Override
    public void execute(ProblemResult problemResult, String userDirPath) {

        if (userDirPath == null) {
            recordService.updateProblemResultStatusById(problemResult.getSubId(), JudgeStatusEnum.Compile_Error.getStatus());
            System.out.println("~~~~~judgeImpl " + 133 + "~~~~~~~" + JudgeStatusEnum.Compile_Error.getDesc());
            return;
        }

//         update judging
        recordService.updateProblemResultStatusById(problemResult.getSubId(), JudgeStatusEnum.Judging.getStatus());
        problemResult.setStatus(JudgeStatusEnum.Judging.getStatus());

        String problemDirPath = fileServerTestcaseDir + envOs + problemResult.getProblemId();
        String inputFileDirPath = problemDirPath + envOs + "input";
        String outputFileDirPath = problemDirPath + envOs + "output";

        Problem problem = recordService.getProblemById(problemResult.getProblemId());
        problem.setTimeLimit(Long.parseLong(problem.getTmLimit()) * 1000L);
        problem.setMemoryLimit(Long.parseLong(problem.getMemLimit()) * 1024L);

        try {
            // 执行输入和输出
            File inputFileDir = new File(inputFileDirPath);
            File[] inputFiles = inputFileDir.listFiles();

            // 并发
            if (inputFiles != null) {
//                System.out.println(inputFileDirPath);

                if (problemResult.getResultMap() == null) {
                    problemResult.setResultMap(new ConcurrentSkipListMap<>());
                }
                CountDownLatch countDownLatch = new CountDownLatch(inputFiles.length);
                ExecutorService executorService = Executors.newFixedThreadPool(inputFiles.length);

                for (File inputFile : inputFiles) {
                    ProcessBuilder builder = CmdUtil.executeCmd(problemResult.getLanguage(), userDirPath);
                    Process process = builder.start();
                    TestInputWork testInputWork = new TestInputWork(problem, inputFile,
                            outputFileDirPath, process, problemResult, countDownLatch);
                    executorService.execute(testInputWork);
                }

                executorService.shutdown();
                countDownLatch.await();
            }

            // 汇总统计
            long maxTime = 0;
            long maxMemory = 0;
            Integer status = null;
            Integer acCount = 0;
            List<TestResult> testResultList = new ArrayList<>();
            Set<Map.Entry<String, TestResult>> entrySet = problemResult.getResultMap().entrySet();

            for (Map.Entry<String, TestResult> entry : entrySet) {
                TestResult testResult = entry.getValue();
//                System.out.println(testResult);
                testResultList.add(testResult);

                if (testResult.getTime() == null) {
                    status = JudgeStatusEnum.Time_Limit_Exceeded.getStatus();
                    continue;
                }

                if (testResult.getMemory() == null) {
                    status = JudgeStatusEnum.Memory_Limit_Exceeded.getStatus();
                    continue;
                }

                if (testResult.getTime() > maxTime) {
                    maxTime = testResult.getTime();
                }

                if (testResult.getMemory() != null&& testResult.getMemory() > maxMemory) {
                    maxMemory = testResult.getMemory();
                }

                if (JudgeStatusEnum.Accept.getStatus().equals(testResult.getStatus())) {
                    // AC
                    ++acCount;
                } else {
                    if (testResult.getMemory() != null && testResult.getMemory() > maxMemory) {
                        maxMemory = testResult.getMemory();
                    }
                    if (testResult.getTime() != null && testResult.getTime() > maxTime) {
                        maxTime = testResult.getTime();
                    }
                    if (JudgeStatusEnum.Runtime_Error.getStatus().equals(testResult.getStatus())) {
                        // RE
                        status = JudgeStatusEnum.Runtime_Error.getStatus();
                        System.out.println("~~~~~judgeImpl " + 226 + "~~~~~~~" + JudgeStatusEnum.Runtime_Error.getDesc());
                    } else if (JudgeStatusEnum.Presentation_Error.getStatus().equals(testResult.getStatus())) {
                        // PE
                        status = JudgeStatusEnum.Presentation_Error.getStatus();
                    } else if (JudgeStatusEnum.Wrong_Answer.getStatus().equals(testResult.getStatus())) {
                        // WA
                        status = JudgeStatusEnum.Wrong_Answer.getStatus();
                    } else if (JudgeStatusEnum.Time_Limit_Exceeded.getStatus().equals(testResult.getStatus())) {
                        // TLE
                        status = JudgeStatusEnum.Time_Limit_Exceeded.getStatus();
                    } else if (JudgeStatusEnum.Memory_Limit_Exceeded.getStatus().equals(testResult.getStatus())) {
                        // MLE
                        status = JudgeStatusEnum.Memory_Limit_Exceeded.getStatus();
                    }
                }
            }

//            System.out.println("AC_count " + acCount);
//            System.out.println("test size " + testResultList.size());
            // AC condition
            if (acCount == testResultList.size() || testResultList.size() == 0) {
                status = JudgeStatusEnum.Accept.getStatus();
            }

            // update problemResult
            problemResult.setRunMemory(maxMemory);
            problemResult.setRunTime(maxTime);
            problemResult.setStatus(status);
            System.out.println("~~~~~judgeImpl " + 244 + "~~~~~~~" + JudgeStatusEnum.getStatusEnum(problemResult.getStatus()).getDesc());
            if (problemResult.getStatus() == JudgeStatusEnum.Compiling.getStatus()) {
                problemResult.setStatus(JudgeStatusEnum.Accept.getStatus());
                System.out.println("~~~~~judgeImpl " + 252 + "~~~~~~~" + JudgeStatusEnum.getStatusEnum(problemResult.getStatus()).getDesc());
            }
            recordService.updateProblemResult(problemResult);

        } catch (Exception e) {
            // 执行脚本错误或没有测试用例或闭锁中断 Exception (update database
            String message = StringUtil.getLimitLenghtByString(e.getMessage(), 1000);
            problemResult.setErrorMsg(message);

            problemResult.setStatus(JudgeStatusEnum.Runtime_Error.getStatus());
            recordService.updateProblemResultStatusById(problemResult.getProblemId(), JudgeStatusEnum.Runtime_Error.getStatus());
            System.out.println("~~~~~judgeImpl " + 262 + "~~~~~~~" + JudgeStatusEnum.getStatusEnum(problemResult.getStatus()).getDesc());

            log.info("执行脚本错误或闭锁终端, e = ", e);
        } finally {
            FileUtil.deleteFile(userDirPath);
        }
    }
}
