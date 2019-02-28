package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DeskLampController {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private UserService userService;

    @RequestMapping("/switch")
    public RetJson switchLampMode(Integer type,Integer eid){
        //此处应该检验此人是否拥有该设备
        equipmentService.switchMode(type,eid);
        return RetJson.succcess(null);
    }

    @RequestMapping("/adjust")
    public RetJson adjustLampBrightness(Integer brightness,Integer eid){
        //此处应该检验此人是否拥有该设备
        equipmentService.adjustBrightness(brightness,eid);
        return RetJson.succcess(null);
    }

    @RequestMapping("/bind")
    public RetJson bindToUser(Integer eid,String macAddress, HttpServletRequest request){
        //参数校验，必须为32位
        User user = (User)request.getAttribute("user");
        Integer id = user.getId();
        String encryptString = Md5Utils.MD5Encode(macAddress,"utf-8",false);
        String tempMacAddress = equipmentService.queryEquipmentById(eid).getMacAddress();
        //设备不存在，或者macAddress不正确


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
