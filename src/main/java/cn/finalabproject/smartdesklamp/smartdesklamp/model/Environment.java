package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Environment {
    private Integer id;

    private float brightness;

    private float noise;

    private float temperature;
    //湿度
    private float humidity;

    private Timestamp time;

    private Integer eid;
}
