package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserInfoMapper {

    @Select("select * from user_info where id=#{id}")
    public UserInfo getUserInfoById(@Param("id") Integer id);

    @Update({"update user_info set phoneNum=#{phoneNum},email=#{email},sex=#{sex},name=#{name},nickName=#{nickName}" +
            " where id=#{id}"})
    public void alterUserInfo(UserInfo userInfo);

    @Update("update user_info set imagePath=#{imagePath} where id=#{id}")
    public Boolean alterHeadPortrait(@Param("id") Integer id, @Param("imagePath") String url);

    @Update("update user_info set background_path=#{backgroundPath} where id=#{id}")
    public boolean alterBackground(@Param("id") Integer id,@Param("backgroundPath") String backgroundPath);
}
