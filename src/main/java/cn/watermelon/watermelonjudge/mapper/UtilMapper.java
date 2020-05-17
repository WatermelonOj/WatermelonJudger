package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.dto.ProblemDTO;
import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UtilMapper {

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId} AND `is_delete` = false AND `result` = 1",
            "ORDER BY `run_time` ASC, `run_memory` ASC",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getGoodPrByProblemId(int problemId);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `user_id` = #{userId} AND `language` = #{language} AND `is_delete` = false",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getUserLanguageNumber(int userId, String language);

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `user_id` = #{userId} AND `result` = #{status} AND `is_delete` = false",
    })
    @Results(value = {
            @Result(property = "status", column = "result"),
            @Result(property = "sourceCode", column = "code"),
            @Result(property = "language", column = "language"),
    })
    List<ProblemResult> getUserStatusNumber(int userId, int status);

    @Select({"SELECT `tag`",
            "FROM `problem_with_tag`",
            "WHERE `problem_id` = #{problemId} ",
    })
    List<String> getProblemTag(int problemId);


    @Select({"SELECT *",
            "FROM `problem`",
    })
    @Results(value = {
            @Result(property = "problemId", column = "problem_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "input", column = "input"),
            @Result(property = "output", column = "output"),
            @Result(property = "contestId", column = "contest_id"),
            @Result(property = "isSpj", column = "spj"),
            @Result(property = "visible", column = "visible"),
            @Result(property = "tmLimit", column = "tm_limit"),
            @Result(property = "memLimit", column = "mem_limit"),
            @Result(property = "sampleInput", column = "sample_input"),
            @Result(property = "sampleOutput", column = "sample_output"),
            @Result(property = "contestId", column = "contest_id"),
    })
    List<ProblemDTO> getProblems();

    @Select({"SELECT *",
            "FROM `problem`",
            "WHERE `problem_id` = #{problemId}",
    })
    @Results(value = {
            @Result(property = "problemId", column = "problem_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "input", column = "input"),
            @Result(property = "output", column = "output"),
            @Result(property = "contestId", column = "contest_id"),
            @Result(property = "isSpj", column = "spj"),
            @Result(property = "visible", column = "visible"),
            @Result(property = "tmLimit", column = "tm_limit"),
            @Result(property = "memLimit", column = "mem_limit"),
            @Result(property = "sampleInput", column = "sample_input"),
            @Result(property = "sampleOutput", column = "sample_output"),
            @Result(property = "contestId", column = "contest_id"),
    })
    ProblemDTO getProblemByPID(int problemId);

    @Select({"SELECT `problem_id`",
            "FROM `problem_with_tag`",
            "WHERE `tag` = #{tag}",
    })
    List<Integer> getProblemsByTag(String tag);

    @Select({"SELECT COUNT(*)",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId} AND `result` = 1",
    })
    int getProblemAcNum(int problemId);

    @Select({"SELECT COUNT(*)",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId}",
    })
    int getProblemSubNum(int problemId);

}
