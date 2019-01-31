package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;

public interface SittingPostureService {
    public int insertPosture(SittingPostureInfo sittingPostureInfo);

    public boolean deletePostureById(@Param("id")Integer id);

    public boolean updatePosture(SittingPostureInfo sittingPostureInfo);

    public SittingPostureInfo[] queryPostures(Timestamp beginTime,Timestamp endTime,Integer uid);

    SittingPostureInfo[] getPosturesByDate(Date date, Integer uid);

    public int getCountByDate(Date date);
}
