package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class SittingPostureInfo {
    int id;
    int uid;
    private int status;//0,1,2,3,4,5,6
    private int score;
    private Timestamp time;
}
