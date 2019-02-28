package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

import java.util.Map;

/**
 * 坐姿数据
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SittingPostureViewObject {
    private Map<Integer,Integer> postures;//坐姿
    private double accuracy;//正确率
    private double average;//平均值
    private int grade;//等级
}
