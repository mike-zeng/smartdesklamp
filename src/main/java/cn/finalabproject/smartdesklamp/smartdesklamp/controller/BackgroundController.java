package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.BackgroundService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BackgroundController {

    private static final Integer MAX_SIZE=5*1024*1024;

    @Autowired
    private BackgroundService backgroundService;
    @Autowired
    private UserService userService;

    /**
     * 删除自定义背景
     * @param bid 背景id
     * @param request
     * @return
     */
    @RequestMapping("/deleteBackground")
    public RetJson deleteBackground(Integer bid,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        //并未在腾讯云删除该图片!
        if (!backgroundService.deleteBackground(uid,bid)){
            return RetJson.fail(-1,"删除失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 修改用户的背景
     * @param bid
     * @param request
     * @return
     */
    @RequestMapping("/alterBackground")
    public RetJson alterUserBackground(Integer bid, HttpServletRequest request){
        Integer id= ((User)request.getAttribute("user")).getId();
        Background background = backgroundService.queryBackgroundByBid(bid);
        if(!id.equals(background.getUid())){
            return RetJson.fail(-1,"非法操作！");
        }
        userService.alterBackground(id,background.getImagePath());
        return RetJson.succcess(null);
    }

    /**
     * 获取所有背景
     * @param request
     * @return
     */
    @RequestMapping("/queryBackgrounds")
    public RetJson queryUserBackgrounds(HttpServletRequest request){
        Integer id= ((User)request.getAttribute("user")).getId();
        Background[] backgrounds = backgroundService.queryBackgrounds(id);
        return RetJson.succcess("backgrounds",backgrounds);
    }

    /**
     *上传用户背景图片
     * @param multipartFile 背景图片文件，不超过5m大小
     * @param request
     * @return
     */
    @RequestMapping("/uploadBackground")
    public RetJson uploadUserBackground(@RequestParam("background") MultipartFile multipartFile, HttpServletRequest request){
        if (multipartFile.getSize()>MAX_SIZE){
            return RetJson.fail(-1,"文件大小超过5兆!");
        }
        //图片校验。。。。留空

        Integer flag=1;
        Integer id= ((User)request.getAttribute("user")).getId();
        Integer size = backgroundService.queryUserBackgrounds(id).length;
        //用户最多可以上传五张背景图片
        if(size >= 5){
            return RetJson.fail(-1,"插入失败！");
        }
        Integer bid = userService.saveUserBackground(multipartFile,id,flag);
        return RetJson.succcess("bid",bid);
    }

}
