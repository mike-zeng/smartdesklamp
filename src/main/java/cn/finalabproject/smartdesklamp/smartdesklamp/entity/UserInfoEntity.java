package cn.finalabproject.smartdesklamp.smartdesklamp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

//entity本该与数据库的字段对应，但是因为已经有了model类，因此在此处交换一下
@Getter
@Setter
@AllArgsConstructor
public class UserInfoEntity {
    @Null
    private  Integer id;
    @NotNull
    private Boolean sex;
    @Length(max = 11,min = 11,message = "手机号长度必须是11位")
    private String phoneNum;
    @Email
    private String email;

    private String imagePath;

    private String name;

    private String nickName;

}
