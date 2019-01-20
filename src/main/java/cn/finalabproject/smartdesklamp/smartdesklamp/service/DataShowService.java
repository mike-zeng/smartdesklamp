package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.vo.BaseDataViewObject;

/**
 * 用来将数据库中的信息处理成可以直接在app展示的数据
 */
public interface DataShowService {
    BaseDataViewObject getBaseData();
}
