package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.BackgroundMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.MusicMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.UserInfoMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.UserMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Background;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Music;
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
import java.util.UUID;

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
    private BackgroundMapper backgroundMapper;
    @Autowired
    private MusicMapper musicMapper;


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
    public Integer[] getAllUserIdList() {
        Integer[] integers=userMapper.getAllUserIdList();
        return integers;
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


    /**
     * 修改 性别 昵称 年龄 地区 信息
     * @param userInfo
     * @return
     */
    @Override
    public boolean alterUserInfo(UserInfo userInfo) {
        UserInfo d=userInfoMapper.getUserInfoById(userInfo.getId());
        if (userInfo.getSex()==null){
            userInfo.setSex(d.getSex());
        }
        if (userInfo.getAge()==null){
            userInfo.setAge(d.getAge());
        }
        if (userInfo.getNickName()==null){
            userInfo.setNickName(d.getNickName());
        }
        if (userInfo.getRegion()==null){
            userInfo.setRegion(d.getRegion());
        }
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
    public boolean alterEquipmentId(Integer eid, Integer id) {
        return userInfoMapper.alterEquipmentId(eid,id);
    }

    @Override
    public Integer getUserIdByEid(Integer eid) {
        return userInfoMapper.getUserIdByEid(eid);
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
            url = uploadFileAndGetUrl(inputStream,"head_portrait/"+username+"_headportrait");
            //写了一个通用函数
            //url=saveUserHeadPortrait(inputStream,username);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (url!=null) {
            return userInfoMapper.alterHeadPortrait(id, url);
        }
        return false;
    }

    @Override
    public Integer saveUserBackground(MultipartFile multipartFile, Integer id,Integer flag) {
        String username=userMapper.getUserByUserId(id).getUsername();
        if (username==null){
            return -1;
        }
        String url=null;
        InputStream inputStream = null;
        try {
            inputStream=multipartFile.getInputStream();
            url = uploadFileAndGetUrl(inputStream,"background/"+ username + "_"+UUID.randomUUID());
            //写了通用的函数
            //url=saveBackground(inputStream,username);
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

    @Override
    public Integer saveUserMusic(MultipartFile multipartFile,Integer id,String musicName) {
        String username = userMapper.getUserByUserId(id).getUsername();
        String url=null;
        InputStream inputStream = null;
        if (username==null){
            return -1;
        }
        try {
            inputStream=multipartFile.getInputStream();
            url = uploadFileAndGetUrl(inputStream,"music/"+ username + "_musicName_"+UUID.randomUUID() + musicName);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(url!=null){
            Music music = new Music();
            music.setUid(id);
            music.setMusicUrl(url);
            music.setMusicName(musicName);
            musicMapper.insertMusic(music);
            return music.getId();
        }
        return -1;
    }

    private String uploadFileAndGetUrl(InputStream inputStream,String key){
        try {
            String url= COSUtils.addFile(key,inputStream);
            return url;
        }finally {
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    private  String saveUserHeadPortrait(InputStream inputStream,String username){
//        try {
//            String url= COSUtils.addFile("head_portrait/"+username+"_headportrait",inputStream);
//            return url;
//        }finally {
//            try {
//                inputStream.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String saveBackground(InputStream inputStream,String username){
//        try {
//            String url= COSUtils.addFile("background/"+ username + "_"+UUID.randomUUID(),inputStream);
//            return url;
//        }finally {
//            try {
//                inputStream.close();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }

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
