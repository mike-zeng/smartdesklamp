package cn.finalabproject.smartdesklamp.smartdesklamp.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 表示用户
 * 说明：role的取值有三个，分别为0,1,2
 * role的默认取值为0，表示一个普通用户，其他取值的意义如下
 * 1：管理员用户
 * 2：根用户
 */
@Validated
@Getter
@Setter
@AllArgsConstructor
public class User {
    @NotNull
    @Length(max = 11, min = 11, message = "手机号的长度必须是11位.")
    private String username;

    @NotNull
    @Length(max = 32,min = 32,message = "密码不合法")
    private String password;

    private String salt;

    private Integer id;

    public User(){

    }

    @Override
    public String toString(){
        String ret=null;
        ObjectMapper mapper=new ObjectMapper();
        try {
            ret=mapper.writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
