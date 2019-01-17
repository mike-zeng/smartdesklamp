package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;

public interface BackgroundService {
    public boolean insertBackground(Background background);

    public boolean deleteBackground(Integer bid);

    public Background[] queryBackgrounds(Integer uid);

    public Background queryBackgroundByBid(Integer bid);
}
