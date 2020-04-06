package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.TestResult;
import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;
import cn.watermelon.watermelonjudge.util.StreamUtil;
import cn.watermelon.watermelonjudge.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * 对用户代码进行运行，并进行评判
 * @author Acerkoo
 * @version 1.0.0
 */
@Slf4j
public class TestInputWork implements Runnable {

    /**
     * 题目
     */
    private Problem problem;

    /**
     * 用户程序执行进程
     */
    private Process process;

    /**
     * 运行结果
     */
    private ProblemResult problemResult;

    /**
     * 输入文件
     */
    private File inputFile;

    /**
     * 输出方向
     */
    private String outputFileDirPath;

    /**
     * 多线程控制
     */
    private CountDownLatch countDownLatch;

    public TestInputWork(Problem problem, File inputFile, String outputFileDirPath, Process process, ProblemResult problemResult, CountDownLatch countDownLatch) {
        this.process = process;
        this.outputFileDirPath = outputFileDirPath;
        this.inputFile = inputFile;
        this.problemResult = problemResult;
        this.countDownLatch = countDownLatch;
        this.problem = problem;
    }

    @Override
    public void run() {
        if (problemResult.getResultMap() == null) {
            problemResult.setResultMap(new ConcurrentSkipListMap<>());
        }

        Map<Integer, TestResult> resultMap = problemResult.getResultMap();
        String inputFileName = inputFile.getName();

        // 样例编号
        Integer testCaseNum = Integer.parseInt(inputFileName.substring(0, inputFileName.lastIndexOf(".")));

        // 测试样例输入
        OutputStream outputStream = process.getOutputStream();
        StreamUtil.setInput(outputStream, inputFile.getPath());

        // 测试样例结果
        TestResult testResult = new TestResult();
        testResult.setCreateTime(new Date());
        testResult.setNum(testCaseNum);
        testResult.setProReId(problemResult.getProblemId());

        // 计算输出时间和内存
        FutureTask<TestResult> task = new FutureTask<>(new TestOutputWork(process, testResult));
        new Thread(task).start();

        try {
            // 计算时间， 等待题目时限 + 200 ms
            int base = 1;
            if (!problemResult.getLanguage().equals(LanguageEnum.C.getType()) && !problemResult.getLanguage().equals(LanguageEnum.CPP.getType())) {
                base = 2;
            }
            System.out.println(problem);
            testResult = task.get(problem.getTimeLimit() * base + 200, TimeUnit.MILLISECONDS);
            if (!JudgeStatusEnum.Runtime_Error.getStatus().equals(testResult.getStatus())) {
                File outputFile = new File(outputFileDirPath + "\\" + inputFileName);
                checkAnswer(problem, outputFile, testResult, base);
            }
            resultMap.put(testCaseNum, testResult);
        } catch (TimeoutException e) {
            log.info("judge is tle");
            process.destroyForcibly();
            // TLE
            testResult.setStatus(JudgeStatusEnum.Time_limit_Exceeded.getStatus());
            resultMap.put(testCaseNum, testResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("judge is re");

            // RE
            testResult.setStatus(JudgeStatusEnum.Runtime_Error.getStatus());
            resultMap.put(testCaseNum, testResult);
        } finally {
            Stream<ProcessHandle> descendants = process.descendants();
            descendants.forEach(ProcessHandle::destroyForcibly);
            countDownLatch.countDown();
        }
    }

    private void checkAnswer(Problem problem, File outputFile, TestResult testResult, int base) {
//        System.out.println(problem);
//        System.out.println(outputFile.getPath());
//        System.out.println(base);
        try {
            if (problem.getTimeLimit() * base < testResult.getTime()) {
                testResult.setStatus(JudgeStatusEnum.Time_limit_Exceeded.getStatus());
                return;
            }
            if (problem.getMemoryLimit() < testResult.getMemory()) {
                testResult.setStatus(JudgeStatusEnum.Memory_Limit_Exceeded.getStatus());
                return;
            }

            String answerOutput = StreamUtil.getOutput(new FileInputStream(outputFile));
            String userOutput = testResult.getUserOutput();

            // 去除最右端多余字符
            answerOutput = StringUtil.rTrim(answerOutput);
            userOutput = StringUtil.rTrim(userOutput);

            if (answerOutput.equals(userOutput)) {
                // AC
                testResult.setStatus(JudgeStatusEnum.Accept.getStatus());
            } else {
                if (StringUtil.formatString(answerOutput).equals(StringUtil.formatString(userOutput))) {
                    // PE
                    testResult.setStatus(JudgeStatusEnum.Presentation_Error.getStatus());
                } else {
                    // WA
                    testResult.setStatus(JudgeStatusEnum.Wrong_Answer.getStatus());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // RE
            testResult.setStatus(JudgeStatusEnum.Runtime_Error.getStatus());
            log.info("when judging , e = ", e.getMessage());
        }
    }

}