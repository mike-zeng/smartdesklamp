package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Music {
    private Integer id;

    private String musicName;

    private Integer uid;

    private String musicUrl;
    //0为系统自带，1为用户自行添加
    private String flag;
}
