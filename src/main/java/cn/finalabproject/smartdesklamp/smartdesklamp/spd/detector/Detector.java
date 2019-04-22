package cn.finalabproject.smartdesklamp.smartdesklamp.spd.detector;

import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.SpdImage;


public interface Detector {
    public boolean init(String appId, String apiKey, String secretKey);

    public Object detection(SpdImage spdImage) throws Exception;
}
