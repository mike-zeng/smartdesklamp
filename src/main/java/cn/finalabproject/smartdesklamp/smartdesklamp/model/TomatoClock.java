package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TomatoClock {
    private Integer id;

    private Integer taskId;

    private Integer duration;

    private Integer incorrectPostureNum;

    private Integer warningTimes;

    private Integer score;

    private Timestamp creatTime;
}
