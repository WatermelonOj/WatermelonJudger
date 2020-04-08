package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissions(int begin, int pageSize);

    @Select({"SELECT COUNT(*)",
            "FROM `submissions`",
            "WHERE `is_delete` = false"
    })
    int getSubmissionsNumber();

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `user_id` = #{userId}",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByUser(int userId, int begin, int pageSize);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `contest_id` = #{contestId}",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByContest(int contestId, int begin, int pageSize);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `problem_id` = #{problemId}",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByProblem(int problemId, int begin, int pageSize);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `status` = #{status}",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByStatus(int status, int begin, int pageSize);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `language` = #{language}",
            "ORDER BY `sub_time` DESC",
            "LIMIT #{begin}, #{pageSize}"
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByLanguage(String language, int begin, int pageSize);

//    @Select({"SELECT *",
//            "FROM `submissions`",
//            "WHERE `is_delete` = false ",
//    })
//    List<ProblemResult> getSubmissions(Integer status);

    @Update({"UPDATE `submissions` set",
            "`run_time` = #{runTime}, `run_memory` = #{runMemory}, `result` = #{status}",
            "WHERE `sub_id` = #{subId} AND `is_delete` = false"})
    int updateByPrimaryKeySelective(ProblemResult record);

    @Update({"UPDATE `submissions` set ",
            "`result` = #{status} ",
            "WHERE `sub_id` = #{subId}"})
    int updateProblemResultStatus(Integer subId, Integer status);

    @Insert({"INSERT into `submissions`",
            "(`problem_id`, `contest_id`, `user_id`, `sub_time`, `run_time`, `run_memory`, `result`, `code`, `language`)",
            "VALUES",
            "(#{problemId}, #{contestId}, #{userId}, #{subTime}, #{runTime}, #{runMemory}, #{status}, #{sourceCode}, #{language})"
    })
    void insertSubmission(ProblemResult record);

    @Select({"SELECT `sub_id` FROM `submissions`",
            "WHERE `is_delete` = false",
            "ORDER BY `sub_time` DESC",
            "LIMIT 0, 1" })
    Integer getProblemResultId();

}
