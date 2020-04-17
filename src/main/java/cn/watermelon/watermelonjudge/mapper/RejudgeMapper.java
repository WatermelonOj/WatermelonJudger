package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    List<ProblemResult> getProblemResultByPID(int problemId);
}
