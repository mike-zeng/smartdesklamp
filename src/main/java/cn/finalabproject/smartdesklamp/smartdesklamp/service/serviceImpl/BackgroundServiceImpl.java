package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.BackgroundMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.BackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackgroundServiceImpl implements BackgroundService {
    @Autowired
    private BackgroundMapper backgroundMapper;
    @Override
    public boolean insertBackground(Background background) {
        Background[] backgrounds = backgroundMapper.queryUserBackgrounds(background.getUid());
        //用户只能上传5张图片
        if(backgrounds.length < 5){
            backgroundMapper.insertBackground(background);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBackground(Integer uid,Integer bid) {

        backgroundMapper.queryBackgroundByBid(uid,bid);
        return backgroundMapper.deleteBackground(uid,bid);
    }

    @Override
    public Background[] queryBackgrounds(Integer uid) {
        return backgroundMapper.queryBackgrounds(uid);
    }

    @Override
    public Background[] querySystemBackgrounds() {
        return new Background[0];
    }

    @Override
    public Background queryBackgroundByBid(Integer uid,Integer bid) {
        return backgroundMapper.queryBackgroundByBid(uid,bid);
    }

    @Override
    public Background[] queryUserBackgrounds(Integer uid) {
        return backgroundMapper.queryUserBackgrounds(uid);
    }
}
