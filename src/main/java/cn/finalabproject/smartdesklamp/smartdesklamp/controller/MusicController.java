package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.MusicService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MusicController {

    private static final Integer MAX_SIZE=5*1024*1024;

    @Autowired
    private MusicService musicService;
    @Autowired
    private UserService userService;
    @Autowired
    EquipmentService equipmentService;

    /**
     * 上传提示音
     * @param multipartFile
     * @param request
     * @return
     */
    @RequestMapping("/uploadMusic")
    public RetJson uploadUserMusic(String musicName,@RequestParam("music") MultipartFile multipartFile, HttpServletRequest request){
        if (multipartFile.getSize()>MAX_SIZE){
            return RetJson.fail(-1,"文件大小超过5兆!");
        }
        //音乐校验
        Integer id= ((User)request.getAttribute("user")).getId();
        Integer size = musicService.queryUserMusicsByUid(id).length;
        if(size >= 5){
            return RetJson.fail(-1,"超过限制，禁止添加！");
        }
        Integer mid = userService.saveUserMusic(multipartFile,id,musicName);
        return RetJson.succcess("musicId",mid);
    }

    @RequestMapping("/getMusics")
    public RetJson getUserMusics(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer id = user.getId();
        Music[] musics = musicService.queryMusicsByUid(id);
        return RetJson.succcess("musics",musics);
    }

    @RequestMapping("/deleteMusic")
    public RetJson deleteMusic(Integer id,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        //并未在腾讯云删除该音乐
        if(musicService.deleteMusicById(id,uid)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"删除失败!");
    }

    @RequestMapping("/alterMusic")
    public RetJson alterCurrentMusic(Integer musicId, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        UserInfo userInfo = (UserInfo)request.getAttribute("userInfo");
        Integer uid = user.getId();
        Integer eid = userInfo.getEid();
        if(eid == null){
            return RetJson.fail(-1,"请先绑定该设备!");
        }
        if(uid.intValue() != eid.intValue()){
            return RetJson.fail(-1,"操作非法！");
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
