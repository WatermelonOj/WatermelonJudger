package cn.watermelon.watermelonjudge.dto;

import lombok.Data;

import java.util.List;

@Data
public class Rank implements Comparable<Rank>{

    int rankId;

    int userId;

    String username;

    int solveNum;

    int penalty;

    List<ProInfo> problem;

    @Override
    public int compareTo(Rank o) {
        if (this.solveNum != o.solveNum) {
            if (this.solveNum > o.solveNum) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (this.penalty > o.penalty) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
