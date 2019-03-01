package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.MusicMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.MusicService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.COSUtils;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Override
    public boolean insertMusic(Music music) {
        return musicMapper.insertMusic(music);
    }

    @Override
    public boolean deleteMusicById(Integer id, Integer uid) {
        Music music=musicMapper.queryMusicById(id);
        String url=music.getMusicUrl();
        String temp[];
        if (musicMapper.deleteMusicById(id,uid)){
            temp=url.split("/");
            COSUtils.deleteFile("music/"+temp[temp.length-1]);
            return true;
        }
        return false;
    }

    @Override
    public Music[] queryMusicsByUid(Integer uid) {
        return musicMapper.queryMusicsByUid(uid);
    }

    @Override
    public Music queryMusicById(Integer id) {
        return musicMapper.queryMusicById(id);
    }

    @Override
    public Music[] queryUserMusicsByUid(Integer uid) {
        return musicMapper.queryUserMusicsByUid(uid);
    }
}
