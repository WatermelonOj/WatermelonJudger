package cn.watermelon.watermelonjudge.util;

/**
 * 字符串格式化工具类
 * @author Acerkoo
 * @version 1.0.0
 */
public class StringUtil {

    public static String lTrim(String original) {
        return null;
    }

    public static String rTrim(String original) {
        if (original == null || "".equals(original)) {
            return original;
        }
        char[] originalChars = original.toCharArray();
        if (originalChars[original.length() - 1] > 32) {
            return original;
        }
        int count = 0;
        for (int i = originalChars.length - 1; i >= 0; --i) {
            if (originalChars[i] > 32) {
                break;
            }
            ++count;
        }

        int length = originalChars.length - count;
        char[] newChars = new char[length];
        System.arraycopy(originalChars, 0, newChars, 0, length);
        return new String(newChars);
    }

    // 限制长度
    public static String getLimitLenghtByString(String string, int maxLength) {
        if (string == null) {
            return null;
        }
        int length = string.length() > maxLength ? maxLength: string.length();
        return string.substring(0, length);
    }

    // windows下文本以\r\n结尾，linux以\n结尾,mac \r 结尾
    public static String formatString(String string, boolean ret) {
        string = string.replace(" ", "");
        if (ret == true) {
            string = string.replace("\n", "");
        }
        string = string.replace("\r", "");
        string = string.replace("\t", "");
        return string;
    }
}
