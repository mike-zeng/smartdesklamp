package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FocusViewObject {
    private double[] focusData;//一天内的专注数据
    private double average;
    private double variance;
    private int grade;
}
