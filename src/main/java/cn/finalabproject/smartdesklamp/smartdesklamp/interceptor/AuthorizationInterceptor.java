package cn.finalabproject.smartdesklamp.smartdesklamp.interceptor;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RedisSession;
import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.ExcludeURI;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.RedisService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.JwtUtils;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author zeng
 * 权限认证,如果失败,则返回授权失败信息
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisService redisService;

    @Autowired
    ExcludeURI excludeURI;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    UserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //排除部分url
        String url=request.getRequestURI();
        if (isExclude(url)){
            return true;
        }

        //获取请求头部中的token
        String token=request.getHeader("Authorization");
        if (token!=null){

            //解密token
            Map<String, Claim> map= JwtUtils.VerifyToken(token);
            if (map==null){
                writeErrorInfo(response);
                return false;
            }
            String uuid=map.get("uuid").asString();
            String id=map.get("id").asString();

            //判断token是否有效
            if (uuid!=null&&id!=null&&redisService.exists("user:"+id)){
                request.setAttribute("user",userService.getUserByUserId(Integer.valueOf(id)));
                request.setAttribute("userInfo",userService.getUserInfo(Integer.valueOf(id)));
                request.setAttribute("id",Integer.valueOf(id));
                String ret=(String) redisService.get("user:"+id);
                if (ret.equals(uuid)||true){
                    //更新过期时间,连续七天不活动则token失效
                    redisService.expire("user:"+id,60*60*24*7);
                    RedisSession redisSession= RedisSession.getInstance(uuid,Long.valueOf(id));
                    if (redisSession!=null){
                        request.setAttribute("redisSession",redisSession);
                    }
                    //设置在线状态
                    if (redisSession!=null&&!url.equals("/offLine")){
                        redisSession.setUserActiveStatu(true);
                    }
                    return true;
                }
            }
        }
        //否则提示token过期,要求重新登录
        writeErrorInfo(response);
        return false;

    }

    private void writeErrorInfo(HttpServletResponse response){
        try {
            Writer writer=response.getWriter();
            writer.write(RetJson.fail(-2,"token已过期,请重新登入").toString());
            writer.flush();
        }catch (Exception e){

        }
        return;
    }

    public boolean isExclude(String uri){
        List<String> list=excludeURI.getExcludeuri();
        if (list.contains(uri)){
            return true;
        }
        return false;
    }

}
