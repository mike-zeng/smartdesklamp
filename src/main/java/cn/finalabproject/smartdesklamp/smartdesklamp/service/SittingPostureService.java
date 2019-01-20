package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface SittingPostureService {
    public int insertPosture(SittingPostureInfo sittingPostureInfo);

    public boolean deletePostureById(@Param("id")Integer id);

    public boolean updatePosture(SittingPostureInfo sittingPostureInfo);

    public SittingPostureInfo[] queryPostures(@Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);


}
