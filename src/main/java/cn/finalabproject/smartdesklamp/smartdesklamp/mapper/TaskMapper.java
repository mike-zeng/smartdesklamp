package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TaskMapper {
    @Insert({"insert into task(uid,begin_time,task_theme,tomato_quantity,completion_status,creat_time) values(" +
            "#{uid},#{beginTime},#{taskTheme},#{tomatoQuantity},#{completionStatus},#{creatTime})"})
    @Options(useGeneratedKeys=true,keyColumn = "id",keyProperty = "id")
    public boolean insertTask(Task task);

    @Delete("delete from task where id=#{id}")
    public boolean deleteTaskById(@Param("id") Integer id);

    @Update("update task set uid=#{uid},begin_time=#{beginTime},task_theme=#{taskTheme},tomato_quantity=#{tomatoQuantity}," +
            "completion_status=#{completionStatus},creat_time=#{creatTime} where id=#{id}")
    public boolean alterTask(Task task);

    @Select("select * from task where id=#{id}")
    @Results(id = "taskMap",value = {
            @Result(column = "begin_time",property = "beginTime"),
            @Result(column = "task_theme",property = "taskTheme"),
            @Result(column = "tomato_quantity",property = "tomatoQuantity"),
            @Result(column = "completion_status",property = "completionStatus"),
            @Result(column = "creat_time",property = "creatTime")
    })
    public Task queryTask(@Param("id") Integer id);

    @Select("select * from task where uid=#{uid}")
    @ResultMap(value = "taskMap")
    public Task[] queryTasksByUid(@Param("uid") Integer uid);
}
