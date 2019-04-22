package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Feedback;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.FeedbackService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.RedisService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class PomodoroController {
    private static final String key="807920489zxh1234";
    User user=null;
    @Autowired
    RedisService redisService;

    @Autowired
    FeedbackService feedbackService;
    @ModelAttribute
    public void comment(HttpServletRequest request){
        user=(User) request.getAttribute("user");
    }

    @RequestMapping("/begin")
    public RetJson beginPomodoro(){
        //检测设备,如果设备正常，则告诉客户端可以使用坐姿检测功能，否则告知客户端不能使用坐姿检测功能
        StringBuffer msg=new StringBuffer();
        Date date=new Date();
        boolean flag=true;
        //判断台灯是否处于工作状态
        msg.append(date.getTime());//添加时间
        msg.append("#");//分割符号
        msg.append(flag);
        String res_key= null;
        try {
            res_key = AESUtil.encrypt(msg.toString(),key);
            return RetJson.succcess("key",res_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RetJson.fail(-1,"后台无法服务");
    }

    @RequestMapping("/end")
    public RetJson endPomodoro(String msg){

        String res= null;
        try {
            res = AESUtil.decrypt(msg,key);
        } catch (Exception e) {
            e.printStackTrace();
            return RetJson.fail(-1,"无法获取反馈信息");
        }
        String[] s=res.split("#");
        if (s.length!=2||s[1].equals("false")){
            return RetJson.fail(-1,"无法获取反馈信息");
        }
        Date startDate=null;
        try {
            startDate=new Date(Long.parseLong(s[0]));
        }catch (Exception e){
            return RetJson.fail(-1,"无法获取反馈信息");
        }
        Feedback feedback=feedbackService.getFeedBack(user.getId(),startDate,new Date());
        return RetJson.succcess("feedback",feedback);
    }
}
