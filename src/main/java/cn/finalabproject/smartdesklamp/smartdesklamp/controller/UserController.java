package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.entity.LampEnvironmentInfoEntity;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.GenerateVerificationCode;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.JwtUtils;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.MoblieMessageUtil;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.ValidatedUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author zeng
 * 与用户操作有关的控制器,如登入注册等
 */
@RestController
public class UserController {
    private static final Integer MAX_SIZE=5*1024*1024;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SignInfoService signInfoService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private SittingPostureService sittingPostureService;
    //登入
    @RequestMapping("/login")
    public RetJson login(User user, HttpServletRequest request){
        if (!ValidatedUtil.validate(user)){
            return RetJson.fail(-1,"登入失败，请检查用户名或密码");
        }
        Boolean b=userService.login(user.getUsername(),user.getPassword());
        if (b==true){
            try {
                user=userService.getUserByUserName(user.getUsername());
                UUID uuid=UUID.randomUUID();
                String token= JwtUtils.createToken(uuid,user.getId().toString());
                redisService.set("user:"+user.getId(),uuid.toString(),60*60*24*7);
                return RetJson.succcess("token",token);
            }catch (Exception e){
                e.printStackTrace();
                return RetJson.fail(-1,"登入失败，服务端错误");
            }
        }else {
            return RetJson.fail(-1,"登入失败,请检查用户名或密码");
        }
    }

    //获取手机验证码
    @RequiresAuthentication
    @RequestMapping("/getcode")
    public RetJson sendIdentifyingCode(@Validated @Length(max = 11, min = 11, message = "手机号的长度必须是11位.")@RequestParam(value = "phonenumber") String phoneNumber){
        if ((userService.findUserByUserName(phoneNumber)!=null)){
            return RetJson.fail(-1,"该用户已经注册");
        }
        String verificationCode = GenerateVerificationCode.generateVerificationCode(4);
        SendSmsResponse response = null;
        try {
            response = MoblieMessageUtil.sendIdentifyingCode(phoneNumber, verificationCode);
        }catch (ClientException e){
            e.printStackTrace();
        }
        String code = response.getCode();
        String message = response.getMessage();
        //在redis中存入用户的账号和对应的验证码
        redisService.set(phoneNumber,verificationCode,300);

        if(code!=null&&code.equals("OK")){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,message);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @RequestMapping("/register")
    public RetJson userRegister(User user, String code) {
        if (!ValidatedUtil.validate(user)) {
            return RetJson.fail(-1, "请检查参数");
        }
//        if (redisService.exists(user.getUsername()) && redisService.get(user.getUsername()).equals(code)) {
            if (true) {
                if (userService.findUserByUserName(user.getUsername()) == null) {
                    userService.register(user);
                    return RetJson.succcess(null);
                }
                return RetJson.fail(-1, "用户已存在！");
            }
//        }
        return RetJson.fail(-1, "验证码不正确！");
    }

    /**
     * 获取用户信息
     * @param id 用户id
     * @param request
     * @return
     */
    @RequestMapping("/getUserInfo")
    public RetJson getUserInfo(Integer id, HttpServletRequest request){
        UserInfo userInfo=userService.getUserInfo(id);
        if (userInfo==null){
            return RetJson.fail(-1,"获取用户信息失败");
        }else{

            return RetJson.succcess("userInfo",userInfo);
        }
    }

    /**
     * 修改用户信息
     * @param userInfo 用户信息，字段为：
     * @param request
     * @return
     */
    @RequestMapping("/alterUserInfo")
    public RetJson alterUserInfo(UserInfo userInfo, HttpServletRequest request){
        if (!ValidatedUtil.validate(userInfo)){
            return RetJson.fail(-1,"请检查参数");
        }
        Integer id = ((User)request.getAttribute("user")).getId();
        userInfo.setId(id);
        UserInfo pastUserInfo = userService.getUserInfo(id);
        copyFieldValue(userInfo,pastUserInfo);
        userService.alterUserInfo(userInfo);
        return RetJson.succcess(null);
    }

    /**
     * 修改用户头像
     * @param multipartFile 头像图片文件，不超过5m
     * @param request
     * @return
     */
    @RequestMapping("/alterHeadPortrait")
    public RetJson alterHeadPortrait(@RequestParam("photo") MultipartFile multipartFile, HttpServletRequest request){
        if (multipartFile.getSize()>MAX_SIZE){
            return RetJson.fail(-1,"文件大小超过5兆!");
        }
        //图片校验。。。。留空
        Integer id= ((User)request.getAttribute("user")).getId();
        userService.saveUserHeadPortrait(multipartFile,id);
        return RetJson.succcess(null);
    }

    /**
     * 绑定邮箱
     * @param email 邮箱地址
     * @param code 邮箱验证码
     * @return
     */
    @RequestMapping("/bindMailbox")
    public RetJson bindMailbox(String email,String code){
        String redisCode=(String) redisService.get(email);
        if (code.equals(redisCode)){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"邮箱验证码错误");
    }

    /**
     * 获取邮箱验证码
     * @param email 邮箱地址
     */
    @RequestMapping("/getEmailCode")
    public RetJson getEmailCode(@RequestParam("email") String email){
        emailService.sentVerificationCode(email);
        return RetJson.succcess(null);
    }

    @RequestMapping("/signIn")
    public RetJson userSignIn(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        if(signInfoService.querySignInfo(uid,date) != null){
            return RetJson.fail(-1,"您已签到！");
        }
        signInfoService.insertSignInfo(new SignInfo(null,uid,date));
        return RetJson.succcess(null);
    }

    @RequestMapping("/getSignInfos")
    public RetJson getUserSignInfos(@DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        SignInfo[] signInfos = signInfoService.querySignInfos(uid,new java.sql.Date(beginDate.getTime()),new java.sql.Date(endDate.getTime()));
        return RetJson.succcess("0",signInfos);
    }

    @RequestMapping("/getCurrentInfo")
    public RetJson getCurrentInfo(Integer eid,HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Integer uid = user.getId();
        Integer workTime = null;
        Integer equipmentUserId = equipmentService.getUserId(eid);
        if(equipmentUserId == null){
            return RetJson.fail(-1,"请先绑定设备！");
        }
        if(uid.intValue() != equipmentUserId.intValue()){
            return RetJson.fail(-1,"非法操作！");
        }
        LampEnvironmentInfoEntity lampEnvironmentInfoEntity = new LampEnvironmentInfoEntity();
        Environment environment = environmentService.queryCurrentEnvironmentInfo(eid);
        Integer musicId = equipmentService.getCurrentMusicId(eid);
        //获取今天凌晨和明天凌晨两个时间点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Timestamp beginDate = new Timestamp(calendar.getTime().getTime());
        calendar.add(Calendar.DATE,1);
        Timestamp endDate = new Timestamp(calendar.getTime().getTime());
        SittingPostureInfo[] sittingPostureInfos = sittingPostureService.queryPostures(beginDate,endDate,uid);
        if(sittingPostureInfos == null){
            workTime = 0;
        }else{
            workTime = sittingPostureInfos.length * 2;
        }
        if(environment != null){
            lampEnvironmentInfoEntity.setBrightness(environment.getBrightness());
            lampEnvironmentInfoEntity.setHumidity(environment.getHumidity());
            lampEnvironmentInfoEntity.setNoise(environment.getNoise());
            lampEnvironmentInfoEntity.setTemperature(environment.getTemperature());
        }
        lampEnvironmentInfoEntity.setWorkTime(workTime);
        lampEnvironmentInfoEntity.setMusicId(musicId);
        return RetJson.succcess("lampEnvironmentInfoEntity",lampEnvironmentInfoEntity);
    }

    public  void copyFieldValue(UserInfo userInfo,UserInfo pastUserInfo){
        for(Field f : userInfo.getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                if(f.get(userInfo) == null&&f.get(pastUserInfo) != null){
                    f.set(userInfo,f.get(pastUserInfo));
                }
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }
}
