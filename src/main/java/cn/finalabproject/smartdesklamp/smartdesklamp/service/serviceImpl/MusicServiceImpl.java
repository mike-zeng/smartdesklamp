package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.MusicMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.MusicService;
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
        return musicMapper.deleteMusicById(id,uid);
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
