package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RejudgeMapper {

    @Update({"UPDATE `submissions`",
            "SET `result` = #{status}",
            "WHERE `problem_id` = #{problemId}"
    })
    void updateStatus(int problemId, int status);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getProblemResultByPID(int problemId);
}
