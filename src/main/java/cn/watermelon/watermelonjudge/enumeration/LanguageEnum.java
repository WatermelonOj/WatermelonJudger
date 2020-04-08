package cn.watermelon.watermelonjudge.enumeration;

/**
 * 语言类型
 * @author Acerkoo
 * @version 1.0.0
 */
public enum LanguageEnum {
    /**
     * Java8语言
     */
    Java8("Java8", "java",true),
    /**
     * C语言
     */
    C("C", "c",true),
    /**
     * Ｃ++语言
     */
    CPP("CPP", "cpp",true),
    /**
     * Python2语言
     */
    Python2("Python2", "py",false),
    /**
     * Python3语言
     */
    Python3("Python3", "py3",false);

    private String type;

    private String ext;

    private boolean requiredCompile;

    LanguageEnum(String type, String ext,boolean requiredCompile) {
        this.type = type;
        this.ext = ext;
        this.requiredCompile = requiredCompile;
    }

    public boolean isRequiredCompile() {
        return requiredCompile;
    }

    public void setRequiredCompile(boolean requiredCompile) {
        this.requiredCompile = requiredCompile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static LanguageEnum getEnumByType(String type) {
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.getType().equals(type)) {
                return languageEnum;
            }
        }
        return null;
    }

}
