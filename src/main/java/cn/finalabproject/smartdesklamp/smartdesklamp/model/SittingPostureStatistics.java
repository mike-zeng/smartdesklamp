package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SittingPostureStatistics {
    private int id;
    private int uid;
    private int t0;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int t6;
    private Date time;
}
