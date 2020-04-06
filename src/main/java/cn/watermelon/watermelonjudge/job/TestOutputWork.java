package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.dto.TestResult;
import cn.watermelon.watermelonjudge.enumeration.JudgeStatusEnum;
import cn.watermelon.watermelonjudge.util.StreamUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 更新用户代码时空消耗
 * @author Acerkoo
 * @version 1.0.0
 */
public class TestOutputWork implements Callable<TestResult> {

    private Process process;

    private TestResult testResult;

    public TestOutputWork(Process process, TestResult testResult) {
        this.process = process;
        this.testResult = testResult;
    }

    @Override
    public TestResult call() throws Exception {

        FutureTask<Long> futureTask = new FutureTask<>(new TestUsedMemoryWork(process));
        new Thread(futureTask).start();;
        Instant startTime = Instant.now();

        // 阻塞
        String output = StreamUtil.getOutput(process.getInputStream());
        Instant endTime = Instant.now();

        testResult.setTime(Duration.between(startTime, endTime).toMillis());
        testResult.setUserOutput(output);

        // 等待进程执行结束，0 代表正常退出
        int exitValue = process.waitFor();
        Long usedMemory = futureTask.get();
//        Long usedMemory = 100L;
        testResult.setMemory(usedMemory);

        if (exitValue != 0 && testResult.getStatus() == null) {
            testResult.setStatus(JudgeStatusEnum.Runtime_Error.getStatus());
        }
        return testResult;
    }
}
