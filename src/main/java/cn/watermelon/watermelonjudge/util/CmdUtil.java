package cn.watermelon.watermelonjudge.util;

import cn.watermelon.watermelonjudge.enumeration.LanguageEnum;

/**
 * terminal 命令执行工具类
 * @author Acerkoo
 * @version 1.0.0
 */
public class CmdUtil {

    public static final String envOs = "/";

    /**
     * 获取编译脚本命令
     * @param type
     * @param dir
     * @return
     */
    public static String compileCmd(String type, String dir) {

        if (LanguageEnum.Java8.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                return "javac " + dir + "/Main.java";
            } else {
                return "javac Main.java";
            }
        } else if (LanguageEnum.C.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                return "gcc -std=c99 " + dir + "/Main.c -o " + dir + "/C.out";
            } else {
                return "gcc -std=c99 Main.c -o C.out";
            }
        } else if (LanguageEnum.CPP.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                return "g++ -std=c++11" + dir + envOs + "Main.cpp -o " + dir + envOs + "C++.out";
            } else {
                return "g++ -std=c++11 Main.cpp -o C++.out";
            }
        } else {
            return null;
        }
    }


    /**
     * 获取运行脚本实例ProcessBuilder
     * @param type
     * @param dir
     * @return
     */
    public static ProcessBuilder executeCmd(String type, String dir) {
        ProcessBuilder builder = null;

        if (LanguageEnum.Java8.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                builder = new ProcessBuilder("java", "-classpath", dir, "/Main");
            } else {
                builder = new ProcessBuilder("java", "Main");
            }
        } else if (LanguageEnum.C.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                builder = new ProcessBuilder(dir + "/C.out");
            } else {
                builder = new ProcessBuilder("C.out");
            }
        } else if (LanguageEnum.CPP.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                builder = new ProcessBuilder(dir + envOs + "C++.out");
            } else {
                builder = new ProcessBuilder("C++.out");
            }
        } else if (LanguageEnum.Python2.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                builder = new ProcessBuilder("python", dir + "/Main.py");
            } else {
                builder = new ProcessBuilder("python", "Main.py");
            }
        } else if (LanguageEnum.Python3.getType().equals(type)) {
            if (dir != null && !"".equals(dir)) {
                builder = new ProcessBuilder("python3", dir + "/Main.py3");
            } else {
                builder = new ProcessBuilder("python3", "Main.py3");
            }
        }

        //设置自动清空流
        builder.redirectErrorStream(true);
        return builder;
    }

}
