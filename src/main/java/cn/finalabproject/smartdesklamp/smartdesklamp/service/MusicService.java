package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;

public interface MusicService {
    //插入提示音
    public boolean insertMusic(Music music);

    //通过id删除提示音
    public boolean deleteMusicById(Integer id,Integer uid);

    //请求所有的提示音（包括系统自带的提示音）
    public Music[] queryMusicsByUid(Integer uid);

    //通过id获取提示音
    public Music queryMusicById(Integer id);

    //通过用户的id获取用户自己的提示音
    public Music[] queryUserMusicsByUid(Integer uid);


}
