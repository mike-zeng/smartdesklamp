package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;

import java.sql.Timestamp;

public interface SittingPostureService {
    public int insertPosture(SittingPostureInfo sittingPostureInfo);

    public boolean deletePostureById(Integer id);

    public boolean updatePosture(SittingPostureInfo sittingPostureInfo);

    public SittingPostureInfo[] queryPostures(Timestamp beginTime,Timestamp endTime,Integer uid);
}
