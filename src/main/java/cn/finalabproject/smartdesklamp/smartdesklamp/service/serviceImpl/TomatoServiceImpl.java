package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.TomatoClockMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.TomatoClock;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TomatoClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TomatoServiceImpl implements TomatoClockService {
    @Autowired
    private TomatoClockMapper tomatoClockMapper;

    @Override
    public boolean creatTomatoClock(TomatoClock tomatoClock) {
        return tomatoClockMapper.creatTomatoClock(tomatoClock);
    }

    @Override
    public boolean deleteTomatoClock(Integer id) {
        return tomatoClockMapper.deleteTomatoClock(id);
    }

    @Override
    public boolean updateTomatoClock(TomatoClock tomatoClock) {
        return tomatoClockMapper.updateTomatoClock(tomatoClock);
    }

    @Override
    public TomatoClock queryTomatoClock(Integer id) {
        return tomatoClockMapper.queryTomatoClock(id);
    }

    @Override
    public TomatoClock[] queryTomatoClocksByTaskId(Integer taskId) {
        return tomatoClockMapper.queryTomatoClocksByTaskId(taskId);
    }
}
