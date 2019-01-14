package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class SittingPostureInfo {
    int id;
    int uid;
    private int status;
    private Timestamp time;
}
