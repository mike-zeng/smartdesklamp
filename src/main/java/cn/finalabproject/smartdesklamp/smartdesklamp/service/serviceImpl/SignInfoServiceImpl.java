package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.SignInfoMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SignInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class SignInfoServiceImpl implements SignInfoService {
    @Autowired
    private SignInfoMapper signInfoMapper;

    @Override
    public boolean insertSignInfo(SignInfo signInfo) {
        return signInfoMapper.insertSignInfo(signInfo);
    }

    @Override
    public SignInfo[] querySignInfos(Integer uid, Date beginDate, Date endDate) {
        return signInfoMapper.querySignInfos(uid,beginDate,endDate);
    }
    @Override
    public SignInfo querySignInfo( Integer uid,Date date){
        return signInfoMapper.querySignInfo(uid,date);
    }
}
