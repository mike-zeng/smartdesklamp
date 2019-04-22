package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Integer wrongCount;//错误次数
    private Integer Frequent;//最频繁的坐姿
    private Double effectiveness;//效率
    private Integer effectiveTime;//有效的时间
}
