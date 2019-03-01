package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Background {
    private Integer bid;

    private Integer uid;

    private String fileName;
    //标记是否为系统背景
    private Integer flag;

    private String imagePath;
}
