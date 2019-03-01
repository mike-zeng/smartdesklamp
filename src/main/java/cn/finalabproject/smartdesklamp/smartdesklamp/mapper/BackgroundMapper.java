package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BackgroundMapper {

    @Insert({"insert into background(uid,flag,file_name,image_path) values(#{uid},#{flag},#{fileName},#{imagePath})"})
    @Options(useGeneratedKeys = true,keyProperty = "bid",keyColumn = "bid")
    public boolean insertBackground(Background background);

    @Delete("delete from background where bid=#{bid} and uid=#{uid}")
    public boolean deleteBackground(@Param("uid")Integer uid,@Param("bid")Integer bid);

    @Select("select * from background where flag=0 or uid=#{uid}")
    @Results({
            @Result(property = "imagePath",column = "image_path")
    })
    public Background[] queryBackgrounds(@Param("uid") Integer uid);

    @Select("select * from background where uid=#{uid}")
    @Results({
            @Result(property = "imagePath",column = "image_path")
    })
    public Background[] queryUserBackgrounds(@Param("uid") Integer uid);//只查询用户自己上传的背景

    @Select("select * from background where bid=#{bid} and uid=#{uid}")
    @Results({
            @Result(property = "imagePath",column = "image_path")
    })
    public Background queryBackgroundByBid(@Param("uid")Integer uid,@Param("bid") Integer bid);
}
