package cn.watermelon.watermelonjudge.job;

import java.util.Comparator;

class A {
    public double rec = 0;
    public int problemId = 0;

    static class MyComparator implements Comparator<A> {
        @Override
        public int compare(A o1, A o2) {
            if (o1.rec > o2.rec)
                return -1;
            else if (o1.rec < o2.rec)
                return 1;
            else
                return 0;
        }
    }
}
