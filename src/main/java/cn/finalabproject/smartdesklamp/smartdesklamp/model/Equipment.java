package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Equipment {
    private Integer equipmentId;

    private Integer kind;

    private String name;

    private String macAddress;

    private Integer musicId;
}
