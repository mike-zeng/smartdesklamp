package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;

public interface BackgroundService {
    //添加自定义背景
    public boolean insertBackground(Background background);

    //删除自定义背景
    public boolean deleteBackground(Integer uid,Integer bid);

    //获取用户背景
    public Background[] queryBackgrounds(Integer uid);

    //查询系统背景
    public Background[] querySystemBackgrounds();

    //通过bid获取背景
    public Background queryBackgroundByBid(Integer uid,Integer bid);

    //查询除系统自带的其他所有背景
    public Background[] queryUserBackgrounds(Integer uid);
}
