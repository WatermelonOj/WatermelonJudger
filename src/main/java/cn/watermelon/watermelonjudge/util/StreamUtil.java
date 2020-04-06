package cn.watermelon.watermelonjudge.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 输入输出流工具类
 * @author Acerkoo
 * @version 1.0.0
 */
@Slf4j
public class StreamUtil {

    public static String getOutput(InputStream inputStream) {
        StringBuilder result = new StringBuilder("");
        try {
            byte[] buffer = new byte[1024];
            int length = 0;
            if (inputStream != null) {
                while ((length = inputStream.read(buffer)) != -1) {
                   result.append(new String(buffer, 0, length));
                }
            }
        } catch (IOException e) {
            log.info("when inputstream reading, e = ", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();;
                } catch (IOException e) {
                    log.info("when inputstream closing execption, e = ", e.getMessage());
                }
            }
        }
        return result.toString();
    }

    public static String setInput(OutputStream outputStream, String inputFilePath) {
        StringBuilder result = new StringBuilder();
        BufferedOutputStream bufferedOutputStream = null;
        FileInputStream fileInputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            fileInputStream = new FileInputStream(inputFilePath);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = fileInputStream.read(buffer)) != -1) {
                result.append(new String(buffer, 0, length));
                bufferedOutputStream.write(buffer, 0, length);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            log.info("whene output writing, e = ", e.getMessage());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                log.info("when close output stream, e = ", e.getMessage());
            }
        }
        return result.toString();
    }

//    public static void main(String []args) {
//        System.out.println();
//    }
}
