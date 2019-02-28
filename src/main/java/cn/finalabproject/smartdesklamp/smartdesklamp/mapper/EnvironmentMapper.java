package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;

@Mapper
public interface EnvironmentMapper {

    @Insert({"insert into environment(brightness,noise,temperature,humidity,time,eid) values(#{brightness},#{noise},#{temperature}," +
            "#{humidity},#{time},#{eid})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public int insertEnvironment(Environment environment);

    @Update("update environment set brightness=#{brightness},noise=#{noise},temperature=#{temperature}," +
            "humidity=#{humidity},time=#{time},eid=#{eid} where id=#{id}")
    public boolean updateEnvironment(Environment environment);

    @Delete("delete from environment where id=#{id}")
    public boolean deleteEnvironment(@Param("id") Integer id);

    @Select("select * from environment where (time between #{lowTime} and #{highTime}) and eid=#{eid}")
    public Environment[] queryEnvironmentsBtTime(@Param("lowTime") Timestamp lowTime,@Param("highTime") Timestamp highTime,Integer eid);

    @Select("select * from environment where id=#{id}")
    public Environment queryEnvironmentById(@Param("id") Integer id);

    @Select("select * from environment where eid=#{eid} order by id desc limit 2")
    public Environment[] queryCurrentEnvironmentInfo(@Param("eid") Integer eid);
}
