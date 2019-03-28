package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyData {
    private int id;
    private int hours;//一天工作时长
    private double avgScore;//平均分数
    private double trend;//比昨天趋势
    private int frequently;//最频繁的动作
}
