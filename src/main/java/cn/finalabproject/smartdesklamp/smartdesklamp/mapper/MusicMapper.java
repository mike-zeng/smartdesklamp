package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MusicMapper {

    @Insert({"insert into music(uid,music_url,music_name) values(#{uid},#{musicUrl},#{musicName})"})
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public boolean insertMusic(Music music);

    @Delete("delete from music where id=#{id} and uid=#{uid}")
    public boolean deleteMusicById(@Param("id")Integer id,@Param("uid")Integer uid);

    @Select("select * from music")
    @Results({
            @Result(column = "music_url",property = "musicUrl"),
            @Result(column = "music_name",property = "musicName")
    })
    public Music[] queryMusicsByUid(@Param("uid") Integer uid);

    @Select("select * from music where uid=#{uid}")
    @Results({
            @Result(column = "music_url",property = "musicUrl"),
            @Result(column = "music_name",property = "musicName")
    })
    public Music[] queryUserMusicsByUid(@Param("uid") Integer uid);

    @Select("select * from music where id=#{id}")
    @Results({
            @Result(column = "music_url",property = "musicUrl"),
            @Result(column = "music_name",property = "musicName")
    })
    public Music queryMusicById(@Param("id") Integer id);
}
