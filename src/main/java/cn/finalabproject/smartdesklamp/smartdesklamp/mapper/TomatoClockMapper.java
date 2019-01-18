package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.TomatoClock;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TomatoClockMapper {
    @Insert({"insert into tomato_clock(task_id,duration,incorrect_posture_num,warning_times,score,creat_time)" +
            "values(#{taskId},#{duration},#{incorrectPostureNum},#{warningTimes},#{score},#{creatTime})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public boolean creatTomatoClock(TomatoClock tomatoClock);

    @Delete("delete from tomato_clock where id=#{id}")
    public boolean deleteTomatoClock(@Param("id") Integer id);

    @Update("update tomato_clock set task_id=#{taskId},duration=#{duration},incorrect_posture_num=#{incorrectPostureNum}" +
            ",warning_times=#{warningTimes},score=#{score},creat_time=#{creatTime} where id=#{id}")
    public boolean updateTomatoClock(TomatoClock tomatoClock);

    @Select("select * from tomato_clock where id=#{id}")
    @Results(id="tomatoClockMap",value = {
            @Result(column = "task_id",property = "taskId"),
            @Result(column = "incorrect_posture_num",property = "incorrectPostureNum"),
            @Result(column = "warning_times",property = "warningTimes"),
            @Result(column = "creat_time",property = "creatTime")
    })
    public TomatoClock queryTomatoClock(@Param("id") Integer id);

    @Select("select * from tomato_clock where task_id=#{taskId}")
    @ResultMap(value = "tomatoClockMap")
    public TomatoClock[] queryTomatoClocksByTaskId(@Param("taskId") Integer taskId);
}
