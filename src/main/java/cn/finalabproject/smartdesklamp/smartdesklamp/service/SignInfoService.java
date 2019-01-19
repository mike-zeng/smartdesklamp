package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SignInfo;

import java.sql.Date;

public interface SignInfoService {
    public boolean insertSignInfo(SignInfo signInfo);

    public SignInfo[] querySignInfos(Integer uid,Date beginDate, Date endDate);

    public SignInfo querySignInfo( Integer uid,Date date);
}
