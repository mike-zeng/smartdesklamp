package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.BackgroundMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.BackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackgroundServiceImpl implements BackgroundService {
    @Autowired
    BackgroundMapper backgroundMapper;
    @Override
    public boolean insertBackground(Background background) {
        Background[] backgrounds = backgroundMapper.queryBackgrounds(background.getUid());
        if(backgrounds.length < 10){
            backgroundMapper.insertBackground(background);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBackground(Integer bid) {
        return backgroundMapper.deleteBackground(bid);
    }

    @Override
    public Background[] queryBackgrounds(Integer uid) {
        return backgroundMapper.queryBackgrounds(uid);
    }

    @Override
    public Background queryBackgroundByBid(Integer bid) {
        return backgroundMapper.queryBackgroundByBid(bid);
    }
}
