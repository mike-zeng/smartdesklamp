package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    private Integer id;

    private Integer uid;

    private Timestamp beginTime;

    private String taskTheme;

    private Integer tomatoQuantity;

    private Integer completionStatus;

    private Timestamp creatTime;
}
