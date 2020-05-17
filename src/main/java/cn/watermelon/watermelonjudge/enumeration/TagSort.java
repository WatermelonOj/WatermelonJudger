package cn.watermelon.watermelonjudge.enumeration;

public enum TagSort {
    SiWei(-1, "思维"),

    Base(0, "基础"),

    SouSuo(1, "搜索"),

    DP(2, "动态规划"),

    String(3, "字符串"),

    Math(4, "数学"),

    DS(5, "数据结构"),

    GRAPH(6, "图论"),

    JiHe(7, "计算几何"),

    Qita(8, "其它"),
        ;

    int type;

    String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    TagSort(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static TagSort getTagSort(int type) {
        TagSort[] tagSorts = TagSort.values();
        for (TagSort tagSort: tagSorts) {
            if (tagSort.getType() == type) {
                return tagSort;
            }
        }
        return null;
    }

}
