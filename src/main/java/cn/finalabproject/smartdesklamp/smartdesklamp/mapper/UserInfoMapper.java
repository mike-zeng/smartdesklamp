package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    public UserInfo getUserInfoById(@Param("id") Integer id);

    @Update({"update user_info set sex=#{sex},nickName=#{nickName},region=#{region} where id=#{id}"})
    public void alterUserInfo(UserInfo userInfo);

    @Update("update user_info set imagePath=#{imagePath} where id=#{id}")
    public Boolean alterHeadPortrait(@Param("id") Integer id, @Param("imagePath") String url);

    @Update("update user_info set background_path=#{backgroundPath} where id=#{id}")
    public boolean alterBackground(@Param("id") Integer id,@Param("backgroundPath") String backgroundPath);

    @Update("update user_info set eid=#{eid} where id=#{id}")
    public boolean alterEquipmentId(@Param("eid") Integer eid,@Param("id") Integer id);

    @Select("select id from user_info where eid=#{eid}")
    public Integer getUserIdByEid(@Param("eid") Integer eid);
}
