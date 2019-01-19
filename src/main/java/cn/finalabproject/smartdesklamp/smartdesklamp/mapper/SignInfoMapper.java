package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SignInfo;
import org.apache.ibatis.annotations.*;

import java.sql.Date;

@Mapper
public interface SignInfoMapper {
    @Insert({"insert into sign_info(uid,sign_date) values(#{uid},#{signDate})"})
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public boolean insertSignInfo(SignInfo signInfo);

    @Select("select * from sign_info where uid=#{uid} and (sign_date >= #{beginDate} and sign_date <= #{endDate})")
    @Results({
            @Result(property = "signDate",column = "sign_date")
    })
    public SignInfo[] querySignInfos(@Param("uid") Integer uid,@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

    @Select("select * from sign_info where uid=#{uid} and sign_date=#{date}")
    @Results({
            @Result(property = "signDate",column = "sign_date")
    })
    public SignInfo querySignInfo(@Param("uid") Integer uid,@Param("date") Date date);
}
