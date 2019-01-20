package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.Date;

@Mapper
public interface SittingPostureMapper {
    @Insert({"insert into sitting_posture(id,uid,status,time) values(#{id},#{uid},#{status},#{time})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public int insertPosture(SittingPostureInfo sittingPostureInfo);

    @Delete("delete from sitting_posture where id=#{id}")
    public boolean deletePostureById(@Param("id")Integer id);

    @Update("update sitting_posture set uid=#{uid},status=#{status},time=#{time}")
    public boolean updatePosture(SittingPostureInfo sittingPostureInfo);

    @Select("select * from sitting_posture where time between #{beginTime} and #{endTime}")
    public SittingPostureInfo[] queryPostures(@Param("beginTime") Timestamp beginTime,@Param("endTime") Timestamp endTime);

    @Select("select count(id) from sitting_posture where to_days(time)=to_days(#{date})")
    public int getCountByDate(Date date);
}
