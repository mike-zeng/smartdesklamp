package cn.finalabproject.smartdesklamp.smartdesklamp.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LampEnvironmentInfoEntity {

    private float brightness;

    private float noise;

    private float temperature;
    //湿度
    private float humidity;

    private Integer musicId;
    //工作时长，单位为分钟
    private Integer workTime;
}
