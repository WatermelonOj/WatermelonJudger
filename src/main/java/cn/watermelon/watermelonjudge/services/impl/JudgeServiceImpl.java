package cn.watermelon.watermelonjudge.services.impl;

import cn.watermelon.watermelonjudge.dto.TestResult;
import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import cn.watermelon.watermelonjudge.job.TestInputWork;
import cn.watermelon.watermelonjudge.services.*;
import cn.watermelon.watermelonjudge.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
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
//    @Value("C:\\Users\\74798\\Desktop\\problem")
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
    public String compile(ProblemResult problemResult) {
        String problemDirpath = fileServerTestcaseDir + envOs + problemResult.getProblemId();
        String userDirPath = problemDirpath + envOs + "submission" + envOs + UUIDUtil.createByTime();
        LanguageEnum languageEnum = LanguageEnum.getEnumByType(problemResult.getLanguage());
        String ext = languageEnum.getExt();
        FileUtil.saveFile(problemResult.getSourceCode(), userDirPath + envOs + "Main." + ext);

        String compileErrorOutput = null;

        problemResult.setStatus(JudgeStatusEnum.Compiling.getStatus());
        int subId = recordService.insertProblemRusult(problemResult);
        problemResult.setSubId(subId);

        if (languageEnum.isRequiredCompile()) {
            try {
                Process process = runtime.exec(CmdUtil.compileCmd(problemResult.getLanguage(), userDirPath));
                compileErrorOutput = StreamUtil.getOutput(process.getErrorStream());
            } catch (IOException e) {
                e.printStackTrace();
                String message = "".equals(e.getMessage()) ? "IOException" : e.getMessage();
                log.info(message);
                compileErrorOutput = message;
            }
        }

        if (compileErrorOutput == null || "".equals(compileErrorOutput)) {
            return userDirPath;
        } else {

            // update compile error
            compileErrorOutput = StringUtil.getLimitLenghtByString(compileErrorOutput, 1000);
            problemResult.setStatus(JudgeStatusEnum.Compile_Error.getStatus());
            problemResult.setErrorMsg(compileErrorOutput);
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
//         update judging
        recordService.updateProblemResultStatusById(problemResult.getSubId(), JudgeStatusEnum.Judging.getStatus());

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
                System.out.println(testResult);

                if (testResult.getTime() > maxTime) {
                    maxTime = testResult.getTime();
                }

                if (testResult.getMemory() > maxMemory) {
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
                testResultList.add(testResult);
            }

//            System.out.println("AC_count " + acCount);
//            System.out.println("test size " + testResultList.size());
            // AC condition
            if (acCount == testResultList.size()) {
                status = JudgeStatusEnum.Accept.getStatus();
            }

            // update problemResult
            problemResult.setRunMemory(maxMemory);
            problemResult.setRunTime(maxTime);
            problemResult.setStatus(status);
            recordService.updateProblemResult(problemResult);

        } catch (Exception e) {
            // 执行脚本错误或没有测试用例或闭锁中断 Exception (update database
            String message = StringUtil.getLimitLenghtByString(e.getMessage(), 1000);
            problemResult.setErrorMsg(message);
            recordService.updateProblemResultStatusById(problemResult.getProblemId(), JudgeStatusEnum.Runtime_Error.getStatus());

            log.info("执行脚本错误或闭锁终端, e = ", e);
        } finally {
            FileUtil.deleteFile(userDirPath);
        }

    }
}
