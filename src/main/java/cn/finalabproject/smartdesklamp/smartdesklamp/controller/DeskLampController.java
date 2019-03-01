package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeskLampController {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private UserService userService;

    User user=null;
    UserInfo userInfo=null;

    @ModelAttribute
    public void common(HttpServletRequest request){
        user=(User)request.getAttribute("user");
        userInfo=(UserInfo)request.getAttribute("userInfo");
    }

    /**
     * 切换模式
     * @param type
     * @return
     */
    @RequestMapping("/switch")
    public RetJson switchLampMode(Integer type){
        Integer eid=userInfo.getEid();
        if (eid==null){
            return RetJson.fail(-1,"请先绑定设备");
        }
        equipmentService.switchMode(type,eid);
        return RetJson.succcess(null);
    }

    /**
     * 手动调节亮度
     * @param brightness
     * @return
     */
    @RequestMapping("/adjust")
    public RetJson adjustLampBrightness(Integer brightness){
        Integer eid=userInfo.getEid();
        if (eid==null){
            return RetJson.fail(-1,"请先绑定设备");
        }
        equipmentService.adjustBrightness(brightness,eid);
        return RetJson.succcess(null);
    }

    /**
     * 设备绑定
     * @param eid
     * @return
     */
    @RequestMapping("/bind")
    public RetJson bindToUser(Integer eid,String md5){
        //参数校验，必须为32位
        if (md5==null||md5.length()!=32){
            return RetJson.fail(-1,"参数错误");
        }
        Integer id = user.getId();
        String encryptString = Md5Utils.MD5Encode(md5,"utf-8",false);
        String tempMacAddress = equipmentService.queryEquipmentById(eid).getMacAddress();
        //设备不存在，或者md5正确
        if(tempMacAddress == null||!tempMacAddress.endsWith(encryptString)){
            return RetJson.fail(-1,"绑定失败！");
        }
        if(userService.alterEquipmentId(eid,id)){
            return RetJson.succcess(null);
        }else {
            return RetJson.fail(-1,"绑定失败。");
        }
    }

}
