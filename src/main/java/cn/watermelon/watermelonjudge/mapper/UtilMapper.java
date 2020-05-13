package cn.watermelon.watermelonjudge.mapper;

import cn.watermelon.watermelonjudge.entity.ProblemResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UtilMapper {

    @Select({"SELECT *",
            "FROM `submissions`",
            "WHERE `problem_id` = #{problemId} AND `is_delete` = false",
            "ORDER BY `run_time` ASC, `run_memory` ASC",
    })
    List<ProblemResult> getGoodPrByProblemId(int problemId);

    @Select({"SELECT COUNT(*)",
            "FROM `submissions`",
            "WHERE `user_id` = #{userId} AND `language` = #{language} AND `is_delete` = false",
    })
    int getUserLanguageNumber(int userId, String language);

    @Select({"SELECT COUNT(*)",
            "FROM `submissions`",
            "WHERE `user_id` = #{userId} AND `language` = #{language} AND `is_delete` = false",
    })
    int getUserStatusNumber(int userId, int status);


}
