package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MarkDataViewObject {
    private double[] score;//数据，24个点
    private double average;//平均值
    private double variance;//方差
    private int grade;//等级
}
