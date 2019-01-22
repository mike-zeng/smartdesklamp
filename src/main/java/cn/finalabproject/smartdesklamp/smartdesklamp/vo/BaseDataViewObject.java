package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

import java.util.Map;

/**
 * 数据展示页第一层
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseDataViewObject {
    private Double score;//评分 以0.5为跨度 如80 80.5 90 90.5 91 91.5
    private Integer grade;//等级 1-10
    private Integer totalTime;//总时间，以分钟为单位
    private Double accuracy;//坐姿正确率
    private Map<Integer,Integer> sittingPostureStatistics;//坐姿统计
}
