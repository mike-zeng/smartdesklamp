package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//EnvironmentInfoViewObject
public class EnvironmentInfoViewObject {

    private float brightness;

    private float noise;

    private float temperature;
    //湿度
    private float humidity;

    private Integer musicId;
    //工作时长，单位为分钟
    private Integer workTime;

    private float brightnessChange;

    private float noiseChange;

    private float temperatureChange;

    private float humidityChange;
}
