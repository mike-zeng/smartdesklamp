package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class MusicController {

    @Autowired
    private MusicService musicService;
    @Autowired
    EquipmentService equipmentService;

    User user=null;
    UserInfo userInfo=null;
    @ModelAttribute
    public void common(HttpServletRequest request){
        user=(User) request.getAttribute("user");
        userInfo = (UserInfo)request.getAttribute("userInfo");
    }

    @RequestMapping("/getMusics")
    public RetJson getUserMusics(){
        Integer id = user.getId();
        Integer eid=userInfo.getEid();
        if (eid==null){
            return RetJson.fail(-1,"暂未绑定设备，请先绑定设备");
        }
        Music[] musics = musicService.queryMusicsByUid(id);
        int currentMusic=equipmentService.getCurrentMusicId(eid);
        Map<String,Object> map=new LinkedHashMap<>();

        map.put("musics",musics);
        map.put("currentMusic",currentMusic);
        return RetJson.succcess(map);
    }


    @RequestMapping("/alterMusic")
    public RetJson alterCurrentMusic(Integer musicId){
        Integer eid = userInfo.getEid();
        if(eid == null){
            return RetJson.fail(-1,"请先绑定该设备!");
        }
        if(musicService.queryMusicById(musicId) == null){
            return RetJson.fail(-1,"当前音乐不存在!");
        }
        if(equipmentService.alterCurrentMusicId(eid,musicId)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"修改失败！");
    }
}
