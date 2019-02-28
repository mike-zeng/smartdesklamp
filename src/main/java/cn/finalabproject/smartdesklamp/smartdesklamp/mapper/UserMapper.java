package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User getUserByUserName(@Param("username") String username);

    @Select("select * from user where id=#{id}")
    public User getUserByUserId(@Param("id") Integer id);


    @Insert({"insert into user(username,password,salt) values(#{username},#{password},#{salt})"})
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void register(User user);

    @Update("update user set oid=#{oid} where id=#{userId}")
    public boolean setOriganization(@Param("oid") Integer oid, @Param("userId") Integer userId);

    @Select("select * from user where id=#{id}")
    public User getUserById(int id);

    @Select("select id from user")
    public Integer[] getAllUserIdList();
}
