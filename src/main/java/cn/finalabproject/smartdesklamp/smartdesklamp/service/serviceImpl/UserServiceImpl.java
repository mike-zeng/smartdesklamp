package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.BackgroundMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.UserInfoMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.UserMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.COSUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    final char []codeSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    final int SALT_LENGTH = 8;//盐值长度
    final int ENCRYPT_NUM=1024;//加密次数

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    BackgroundMapper backgroundMapper;



    @Override
    public Boolean login(String userName, String password) {

        Subject currentUser = SecurityUtils.getSubject();


        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        try {
            currentUser.login(token);//登入验证
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean logout(){
        Subject currentUser=SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()){
            currentUser.logout();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }


    @Override
    public void register(User user)
    {
        String password = user.getPassword();
        String salt=produceSalt();//生成八位的盐值
        ByteSource byteSource=ByteSource.Util.bytes(salt);
        SimpleHash simpleHash=new SimpleHash("md5",password,byteSource,ENCRYPT_NUM);
        user.setPassword(simpleHash.toHex());
        user.setSalt(salt);
        userMapper.register(user);
    }


    @Override
    public boolean alterUserInfo(UserInfo userInfo) {
        //判断是否为本人操作
        userInfoMapper.alterUserInfo(userInfo);
        return true;

    }

    @Override
    public UserInfo getUserInfo(Integer id) {
        UserInfo userInfo = userInfoMapper.getUserInfoById(id);
        return userInfo;
//        return new UserInfoEntity(userInfo.getId(),userInfo.getSex(),userInfo.getEmail(),userInfo.getNickName());
    }

    @Override
    public User getUserByUserId(Integer valueOf) {
        return userMapper.getUserByUserId(valueOf);
    }

    @Override
    public boolean alterBackground(Integer id, String backgroundPath) {
        return userInfoMapper.alterBackground(id,backgroundPath);
    }

    @Override
    public boolean saveUserHeadPortrait(MultipartFile multipartFile, Integer id) {
        String username=userMapper.getUserByUserId(id).getUsername();
        if (username==null){
            return false;
        }
        InputStream inputStream=null;
        String url=null;
        try {
            inputStream=multipartFile.getInputStream();
            url=saveUserHeadPortrait(inputStream,username);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (url!=null) {
            return userInfoMapper.alterHeadPortrait(id, url);
        }
        return false;
    }

    @Override
    public Integer saveUserBackground(MultipartFile multipartFile, Integer id,Integer size,Integer flag) {
        String username=userMapper.getUserByUserId(id).getUsername();
        if (username==null){
            return -1;
        }
        String url=null;
        InputStream inputStream = null;
        try {
            inputStream=multipartFile.getInputStream();
            url=saveBackground(inputStream,username,size);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (url!=null) {
            Background background = new Background();
            background.setImagePath(url);
            background.setFlag(1);
            background.setUid(id);
            backgroundMapper.insertBackground(background);
            return background.getBid();
        }
        return -1;
    }

    private  String saveUserHeadPortrait(InputStream inputStream,String username){
        try {
            String url= COSUtils.addFile("head_portrait/"+username+"_headportrait",inputStream);
            return url;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String saveBackground(InputStream inputStream,String username,Integer size){
        try {
            String url= COSUtils.addFile("head_portrait/"+ username +"_headportrait" + size,inputStream);
            return url;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String produceSalt()
    {
        StringBuilder randomString= new StringBuilder();
        Random random = new Random();
        for(int i = 0;i < SALT_LENGTH;i++)
        {
            String strRand = null;
            strRand = String.valueOf(codeSequence[random.nextInt(62)]);
            randomString.append(strRand);
        }
        return randomString.toString();
    }


}
