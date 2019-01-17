package cn.finalabproject.smartdesklamp.smartdesklamp.service;


import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    public Boolean login(String userName, String password);

    public User findUserByUserName(String userName);

    public User getUserByUserName(String userName);

    public Boolean logout();

    public void register(User user);

    public boolean saveUserHeadPortrait(MultipartFile multipartFile, Integer id);

    public boolean alterUserInfo(UserInfo userInfo);

    public Integer saveUserBackground(MultipartFile multipartFile, Integer id,Integer flag);

    public UserInfo getUserInfo(Integer id);

    User getUserByUserId(Integer valueOf);

    public boolean alterBackground(Integer id,String backgroundPath);
}
