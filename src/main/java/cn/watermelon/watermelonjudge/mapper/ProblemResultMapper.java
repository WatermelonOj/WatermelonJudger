package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.*;

/**
 * @author Acerkoo
 * @version 1.0.0
 */
@Mapper
public interface ProblemResultMapper {

    @Select({"SELECT *",
            "FROM `problem`",
            "where `problem_id` = #{problemId}"})
    @Results(value = {
            @Result(property = "problemId", column = "problem_id"),
            @Result(property = "timeLimit", column = "time_limit"),
            @Result(property = "memoryLimit", column = "memory_limit"),
    })
    Problem getProblemById(Integer problemId);

    @Update({"UPDATE `submissions` set",
            "`run_time` = #{runTime}, `run_memory` = #{runMemory}, `result` = #{status}",
            "WHERE `sub_id` = #{subId}"})
    int updateByPrimaryKeySelective(ProblemResult record);

    @Update({"UPDATE `submissions` set ",
            "`result` = #{status} ",
            "WHERE `sub_id` = #{subId}"})
    int updateProblemResultStatus(@Param("subId") Integer subId, @Param("status") Integer status);

    @Insert({"INSERT into `submissions`",
            "(`problem_id`, `contest_id`, `user_id`, `sub_time`, `run_time`, `run_memory`, `result`, `code`, `language`)",
            "VALUES",
            "(#{problemId}, #{contestId}, #{userId}, #{subTime}, #{runTime}, #{runMemory}, #{status}, #{sourceCode}, #{language})"
    })
    void insertSubmission(ProblemResult record);

    @Select({"SELECT `sub_id` FROM `submissions`",
            "ORDER BY `sub_time` DESC",
            "LIMIT 0, 1" })
    Integer getProblemResultId();

}
