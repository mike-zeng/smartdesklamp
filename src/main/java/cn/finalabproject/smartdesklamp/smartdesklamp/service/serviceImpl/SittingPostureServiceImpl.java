package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.SittingPostureMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SittingPostureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class SittingPostureServiceImpl implements SittingPostureService {

    @Autowired
    SittingPostureMapper sittingPostureMapper;

    @Override
    public int insertPosture(SittingPostureInfo sittingPostureInfo) {
        return sittingPostureMapper.insertPosture(sittingPostureInfo);
    }

    @Override
    public boolean deletePostureById(Integer id) {
        return sittingPostureMapper.deletePostureById(id);
    }

    @Override
    public boolean updatePosture(SittingPostureInfo sittingPostureInfo) {
        return sittingPostureMapper.updatePosture(sittingPostureInfo);
    }

    @Override
    public SittingPostureInfo[] queryPostures(Timestamp beginTime, Timestamp endTime,Integer uid) {
        return sittingPostureMapper.queryPostures(beginTime,endTime,uid);
    }
    @Override
    public SittingPostureInfo[] getPosturesByDate(Date date,Integer uid){
        return sittingPostureMapper.queryPosturesByDate(date,uid);
    }
    @Override
    public int getCountByDate(Date date) {
        return sittingPostureMapper.getCountByDate(date);
    }
}
