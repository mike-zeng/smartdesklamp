package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Null
    private  Integer id;

    private Integer age;
    @NotNull
    private Boolean sex;
    @Length(max = 11,min = 11,message = "手机号长度必须是11位")
    private String phoneNum;
    @Email
    private String email;

    private String imagePath;

    private String name;

    private String nickName;

    private String backgroundPath;
}
