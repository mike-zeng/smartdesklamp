package cn.finalabproject.smartdesklamp.smartdesklamp.shiro;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //登入验证逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken)token;
        String username=usernamePasswordToken.getUsername();
        User dbuser=userService.findUserByUserName(username);

        if (dbuser==null){
            return null;//登入失败
        }
        String realmNam=getName();
        ByteSource salt = ByteSource.Util.bytes(dbuser.getSalt());
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(dbuser.getUsername(),dbuser.getPassword(),salt,realmNam);
        return info;
    }

    //权限添加逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal=principalCollection.getPrimaryPrincipal();
        Set<String> roles=new HashSet<>();
        roles.add("user");
        if ("admin".equals(principal)){
            roles.add("admin");
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo(roles);
        return info;
    }
}
