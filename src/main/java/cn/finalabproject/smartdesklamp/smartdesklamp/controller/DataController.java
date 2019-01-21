package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.DataShowService;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.BaseDataViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DataController {
    @Autowired
    DataShowService dataShowService;
    @RequestMapping("/getBaseData")
    public RetJson getBaseData(HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        BaseDataViewObject baseDataViewObject=dataShowService.getBaseData(uid);
        return RetJson.succcess("baseDataViewObject",baseDataViewObject);
    }

}
