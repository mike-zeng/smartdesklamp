package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.BackgroundService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    User user=null;

    @ModelAttribute
    public void comment(HttpServletRequest request){
        user=(User) request.getAttribute("user");
    }

    /**
     * 删除自定义背景
     * @param bid 背景id
     * @return
     */
    @RequestMapping("/deleteBackground")
    public RetJson deleteBackground(Integer bid){
        if (!backgroundService.deleteBackground(user.getId(),bid)){
            return RetJson.fail(-1,"删除失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 修改用户的背景
     * @param bid
     * @return
     */
    @RequestMapping("/alterBackground")
    public RetJson alterUserBackground(Integer bid){
        Background background = backgroundService.queryBackgroundByBid(user.getId(),bid);
        userService.alterBackground(user.getId(),background.getImagePath());
        return RetJson.succcess(null);
    }

    /**
     * 获取所有背景
     * @return
     */
    @RequestMapping("/queryBackgrounds")
    public RetJson queryUserBackgrounds(){
        Background[] backgrounds = backgroundService.queryBackgrounds(user.getId());
        return RetJson.succcess("backgrounds",backgrounds);
    }

    /**
     *上传用户背景图片
     * @param multipartFile 背景图片文件，不超过5m大小
     * @return
     */
    @RequestMapping("/uploadBackground")
    public RetJson uploadUserBackground(@RequestParam("background") MultipartFile multipartFile){
        if (multipartFile.getSize()>MAX_SIZE){
            return RetJson.fail(-1,"文件大小超过5兆!");
        }
        Integer flag=1;
        Integer size = backgroundService.queryUserBackgrounds(user.getId()).length;
        //用户最多可以上传五张背景图片
        if(size >= 5){
            return RetJson.fail(-1,"插入失败！");
        }
        Integer bid = userService.saveUserBackground(multipartFile,user.getId(),flag);
        return RetJson.succcess("bid",bid);
    }

}
