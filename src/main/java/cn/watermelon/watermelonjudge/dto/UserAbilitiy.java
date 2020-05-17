package cn.watermelon.watermelonjudge.dto;

import lombok.Data;

@Data
public class UserAbilitiy {

    int userId;

    int type;

    int num;

    String ability;

    int level;

    public void addOne() {
        this.num = this.num + 1;
    }

}
