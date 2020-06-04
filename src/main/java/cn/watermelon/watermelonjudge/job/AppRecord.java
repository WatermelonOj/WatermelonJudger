package cn.watermelon.watermelonjudge.job;

import java.util.Comparator;

class AppRecord {
    public double rec = 0;
    public int problemId = 0;

    static class MyComparator implements Comparator<AppRecord> {
        @Override
        public int compare(AppRecord o1, AppRecord o2) {
            if (o1.rec > o2.rec)
                return -1;
            else if (o1.rec < o2.rec)
                return 1;
            else
                return 0;
        }
    }
}
