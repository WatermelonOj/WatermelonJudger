package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.Problem;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.*;

import java.util.Date;
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
            @Result(property = "tmLimit", column = "tm_limit"),
            @Result(property = "memLimit", column = "mem_limit"),
    })
    Problem getProblemById(Integer problemId);

    @Select({"SELECT `code`",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId} AND `user_id` = #{userId} AND `is_delete` = false",
            "ORDER BY `sub_time` DESC",
            "LIMIT 0, 1",
    })
    String getUserLastSubmission(int problemId, int userId);

    @Select({"SELECT `username`",
            "FROM `user`",
            "WHERE `user_id` = #{userId}",
    })
    String getUsername(int userId);

    @Select({"SELECT `start_time`",
            "FROM `contest`",
            "WHERE `contest_id` = #{contestId}",
    })
    Date getContestStartTime(int contestId);

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
            "WHERE `is_delete` = false",
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
            "WHERE `is_delete` = false && `user_id` = #{userId}",
            "ORDER BY `sub_time` DESC",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getAllSubmissionsByUser(int userId);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false AND `user_id` = #{userId} AND `result` = 1",
            "ORDER BY `sub_time` DESC",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getAllAcSubmissionsByUser(int userId);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false AND `user_id` = #{userId} AND `result` = 1 AND `problem_id` = #{problemId}",
            "ORDER BY `sub_time` DESC",
            "LIMIT 0, 1",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    ProblemResult getProAcSubmissionsByUser(int problemId, int userId);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `is_delete` = false && `contest_id` = #{contestId}",
            "ORDER BY `sub_time` ASC",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getSubmissionsByContestId(int contestId);

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
            "WHERE `sub_id` = #{subId}"})
    int updateByPrimaryKeySelective(ProblemResult record);

    @Update({"UPDATE `submissions` set ",
            "`result` = #{status} ",
            "WHERE `sub_id` = #{subId}"})
    int updateProblemResultStatus(Integer subId, Integer status);

    @Insert({"INSERT into `submissions`",
            "(`problem_id`, `contest_id`, `user_id`, `sub_time`, `run_time`, `run_memory`, `result`, `code`, `language`, `is_delete`)",
            "VALUES",
            "(#{problemId}, #{contestId}, #{userId}, #{subTime}, #{runTime}, #{runMemory}, #{status}, #{sourceCode}, #{language}, #{rejudge})"
    })
    void insertSubmission(ProblemResult record);

    @Select({"SELECT `sub_id` FROM `submissions`",
            "WHERE `is_delete` = false",
            "ORDER BY `sub_time` DESC",
            "LIMIT 0, 1" })
    Integer getProblemResultId();

}
