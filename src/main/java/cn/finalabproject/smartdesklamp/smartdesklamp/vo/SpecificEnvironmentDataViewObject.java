package cn.finalabproject.smartdesklamp.smartdesklamp.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpecificEnvironmentDataViewObject {
    private Float[] temperature;

    private Float[] humidity;

    private Float[] noise;

    private Float[] brightness;
}
