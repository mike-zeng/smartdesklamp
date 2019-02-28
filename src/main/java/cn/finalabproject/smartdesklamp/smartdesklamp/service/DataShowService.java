package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.vo.*;

import java.util.Date;

/**
 * 用来将数据库中的信息处理成可以直接在app展示的数据
 */
public interface DataShowService {
    BaseDataViewObject getBaseData(Integer uid);

    EnvironmentInfoViewObject getEnvironmentData(Integer eid);

    SpecificEnvironmentDataViewObject getSpecificData(Date date,Integer eid);

    SittingPostureViewObject getSittingPostureData(Integer uid, Date date);

    StudyTimeViewObject getStudyTimeData(Integer uid, Date date);

    MarkDataViewObject getMarkData(Integer uid,Date date);

    FocusViewObject getFocusData(Integer uid,Date date);
}
