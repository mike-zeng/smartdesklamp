package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.TomatoClock;

public interface TomatoClockService {
    public boolean creatTomatoClock(TomatoClock tomatoClock);

    public boolean deleteTomatoClock(Integer id);

    public boolean updateTomatoClock(TomatoClock tomatoClock);

    public TomatoClock queryTomatoClock(Integer id);

    public TomatoClock[] queryTomatoClocksByTaskId(Integer taskId);
}
