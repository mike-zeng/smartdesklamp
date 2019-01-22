package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudyTimeViewObject {
    private int[] time;//表示从周一到周日的学习时间
    private int totalTime;//本周总时间
    private double average;//平均值
    private int grade;//等级
}
