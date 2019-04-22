package cn.finalabproject.smartdesklamp.smartdesklamp.spd.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.HeadInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.SittingPosition;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.UpperBodyInfo;

/**
 * 坐姿检测
 */
public interface SittingPositionDetection {
    public SittingPosition getSittingPosition(HeadInfo headInfo, UpperBodyInfo upperBodyInfo);
}
