package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SignInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class DataController {
    @Autowired
    private DataShowService dataShowService;
    @Autowired
    private SignInfoService signInfoService;
    @Autowired
    private SittingPostureService sittingPostureService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EnvironmentService environmentService;
    User user=null;

    @ModelAttribute
    public void comment(HttpServletRequest request){
        user=(User) request.getAttribute("user");
    }

    @RequestMapping("/getBaseData")
    public RetJson getBaseData(HttpServletRequest request){
        Integer uid=user.getId();
        BaseDataViewObject baseDataViewObject=dataShowService.getBaseData(uid);
        return RetJson.succcess("baseDataViewObject",baseDataViewObject);
    }


    @RequestMapping("/getSignInfos")
    public RetJson getUserSignInfos(@DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        SignInfo[] signInfos = signInfoService.querySignInfos(uid,new java.sql.Date(beginDate.getTime()),new java.sql.Date(endDate.getTime()));
        return RetJson.succcess("signInfos",signInfos);
    }

    @RequestMapping("/getCurrentInfo")
    public RetJson getCurrentInfo(Integer eid,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        Integer equipmentUserId = equipmentService.getUserId(eid);
        if(equipmentUserId == null){
            return RetJson.fail(-1,"请先绑定设备！");
        }
        if(uid.intValue() != equipmentUserId.intValue()){
            return RetJson.fail(-1,"非法操作！");
        }
        EnvironmentInfoViewObject environmentInfoViewObject = dataShowService.getEnvironmentData(eid);
        return RetJson.succcess("environmentInfoViewObject", environmentInfoViewObject);
    }

    @RequestMapping("/getSpecificInfo")
    public RetJson getSpecificInfo(@DateTimeFormat(pattern = "yyyy-MM-dd")Date date,Integer eid,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        Integer equipmentUserId = equipmentService.getUserId(eid);
        if(equipmentUserId == null){
            return RetJson.fail(-1,"请绑定设备！");
        }
        if(uid.intValue() != equipmentUserId.intValue()){
            return RetJson.fail(-1,"非法操作！");
        }
        SpecificEnvironmentDataViewObject specificEnvironmentDataViewObject = dataShowService.getSpecificData(date,eid);
        return RetJson.succcess("specificData",specificEnvironmentDataViewObject);
    }

    @RequestMapping("/getSittingPostureData")
    public RetJson getSittingPostureData(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        SittingPostureViewObject sittingPostureViewObject=dataShowService.getSittingPostureData(uid,date);
        return RetJson.succcess("sittingPostureData",sittingPostureViewObject);
    }

    @RequestMapping("/getMarkData")
    public RetJson getMarkData(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date, HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        MarkDataViewObject markDataViewObject=dataShowService.getMarkData(uid,date);
        return RetJson.succcess("markData",markDataViewObject);
    }

    @RequestMapping("/getStudyTimeData")
    public RetJson getStudyTimeData(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        StudyTimeViewObject studyTimeViewObject=dataShowService.getStudyTimeData(uid,date);
        return RetJson.succcess("studyTimeData",studyTimeViewObject);
    }


    @RequestMapping("/getFocusData")
    public RetJson getFocusData(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        FocusViewObject focusViewObject=dataShowService.getFocusData(uid,date);
        return RetJson.succcess("focusData",focusViewObject);
    }

}
