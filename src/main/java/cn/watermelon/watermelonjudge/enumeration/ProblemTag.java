package cn.watermelon.watermelonjudge.enumeration;

public enum ProblemTag {

    SiWei(-1, "思维题"),

    MeiJu(0, "枚举"),

    MoNi(0, "模拟"),

    DiGui(0, "递归"),

    FenZhi(0, "分治"),

    PaiXu(0, "排序"),

    ErFen(0, "二分"),

    BeiZeng(0, "倍增"),

    QianZhuiHe(0, "前缀和"),

    DFS(1, "深度优先搜索"),

    BFS(1, "广度优先搜索"),

    ShuangXiang(1, "双向搜索"),

    QiFaSouSuo(1, "启发式搜索"),

    AStar(1, "A*"),

    IDA(1, "IDA"),

    DancingLinks(1, "DancingLinks"),

    JiYiHua(1, "记忆化搜索"),

    BeiBaoDP(2, "背包 DP"),

    QuJianDP(2, "区间 DP"),

    DAG_DP(2, "DAG 上的 DP"),

    TreeDP(2, "树形 DP"),

    ZhuangYa(2, "状压 DP"),

    ShuWei(2, "数位 DP"),

    Chatou(2, "插头 DP"),

    JiShu(2, "计数 DP"),

    DongTai(2, "动态 DP"),

    March(3, "字符串匹配"),

    Hash(3, "字符串哈希"),

    Trie(3, "字典树"),

    KMP(3, "KMP 算法"),

    EXKMP(3, "拓展 KMP"),

    ACM(3, "AC 自动机"),

    SA(3, "后缀数组"),

    SAM(3, "后缀自动机"),

    EXSAM(3, "广义后缀自动机"),

    Manacher(3, "马拉车"),

    HuiWen(3, "回文树"),

    XuLieM(3, "序列自动机"),

    MiMx(3, "最小表示法"),

    Bit(4, "位运算"),

    Ksm(4, "快速幂"),

    GaoJingDu(4, "高精度"),

    ShuLun(4, "数论"),

    DuoXiangShi(4, "多项式"),

    XianDai(4, "线性代数"),

    XianGui(4, "线性规划"),

    GaiLv(4, "概率 & 期望"),

    ZhiHuanQun(4, "置换群"),

    FiBo(4, "斐波那契数列"),

    BoYi(4, "博弈"),

    NiuDun(4, "牛顿迭代法"),

    JiFen(4, "数值积分"),

    DaBiao(4, "分段打表"),

    Stack(5, "栈"),

    Queue(5, "队列"),

    List(5, "链表"),

    BingCha(5, "并查集"),

    Heap(5, "堆"),

    Kuai(5, "分块数据结构"),

    DanDiao(5, "单调队列/栈"),

    ST(5, "ST 表"),

    BIT(5, "树状数组"),

    SegmentTree(5, "线段树"),

    HuaFenTree(5, "划分树"),

    ErCha(5, "二叉搜索树 & 平衡树"),

    ChiJiu(5, "可持久化数据结构"),

    TreeAndTree(5, "树套树"),

    KDTree(5, "K-D Tree"),

    KeDuoLi(5, "珂朵莉树"),

    DongTaiTree(5, "动态树"),

    XiHe(5, "析合树"),

    DFS_G(6, "DFS（图论）"),

    BFS_G(6, "BFS（图论）"),

    OnTree(6, "树上问题"),

    JuZhenShu(6, "矩阵书定理"),

    DAG(6, "有向无环图"),

    Topo(6, "拓扑排序"),

    MST(6, "最小生成树"),

    MSG(6, "最小树形图"),

    MSL(6, "最短路"),

    ChaiDian(6, "拆点"),

    ChaFenYueShu(6, "差分约束"),

    KthShort(6, "K 短路"),

    LianTong(6, "连通性"),

    TwoSat(6, "2-SAT"),

    Oula(6, "欧拉图"),

    Hamidon(6, "哈密顿图"),

    ErFenG(6, "二分图"),

    MMC(6, "最小环"),

    PingMianTu(6, "平面图"),

    ZhuoSe(6, "图的着色"),

    WangLuoLiu(6, "网络流"),

    Prufer(6, "Prufer 序列"),

    LGV(6, "LGV 序列"),

    XianTu(6, "弦图"),

    ErWeiJiHe(7, "二维计算几何基础"),

    SanWeiJiHe(7, "三维计算几何基础"),

    JiZuoBiao(7, "极坐标系"),

    Distance(7, "距离"),

    Pick(7, "Pick 定理"),

    SanJiao(7, "三角剖分"),

    TuBao(7, "凸包"),

    SaoMiao(7, "扫描线"),

    XZKK(7, "旋转卡壳"),

    BanPingMianJiao(7, "半平面交"),

    MDPair(7, "平面最近点对"),

    RandomAdd(7, "随机增量法"),

    FanYanBianHuan(7, "反演变换"),

    JiSuanZaXiang(7, "计算几何杂项"),

    ReadWrite(8, "读入输出优化"),

    LiSan(8, "离散化"),

    LiXian(8, "离线算法"),

    FenShuGuiHua(8, "分数规划"),

    Random(8, "随机化"),

    XuanXian(8, "悬线法"),

    LiLun(8, "计算理论基础"),

    ZiJie(8, "字节顺序"),

    YueSeFu(8, "约瑟夫问题"),

    QiuZhi(8, "表达式求值");

    private int type;

    private String desc;

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }

    ProblemTag(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ProblemTag getProblemTag(String tag) {
        ProblemTag[] problemTags = ProblemTag.values();
        for (ProblemTag problemTag: problemTags) {
            if (problemTag.getDesc().equals(tag)) {
                return problemTag;
            }
        }
        return null;
    }
}
