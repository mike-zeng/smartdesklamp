package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignInfo {
    private Integer id;

    private Integer uid;

    private Date signDate;
}
