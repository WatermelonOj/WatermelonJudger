package cn.watermelon.watermelonjudge.job;

import cn.watermelon.watermelonjudge.util.StreamUtil;

import java.util.concurrent.Callable;

/**
 * 获取用户空间消耗
 * @author Acerkoo
 * @version 1.0.0
 */
public class TestUsedMemoryWork implements Callable<Long> {

    private Process process;

    public TestUsedMemoryWork(Process process) {
        this.process = process;
    }

    @Override
    public Long call() throws Exception {
        String cmd = "cat   /proc/" + process.pid() + "/status | grep VmRSS | tr -cd '[0-9]' ";
        Long max = 0L;
        while (process.isAlive()) {
            try {
                Process countUsedMemoryProcess = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
                String memory = StreamUtil.getOutput(countUsedMemoryProcess.getInputStream());
                long aLong = 0;
                try {
                    aLong = Long.parseLong(memory);
                } catch (Exception e){
                    continue;
                }
                if (aLong > max) {
                    max = aLong;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return max;
    }
}
