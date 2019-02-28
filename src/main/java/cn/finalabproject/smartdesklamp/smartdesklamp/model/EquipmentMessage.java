package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class EquipmentMessage {

    private Integer equipmentId;

    private float brightness;

    private float noise;

    private float temperature;

    private float humidity;
    //String str = Base64.encodeToString(mBuff,Base64.DEFAULT);将图片用base64编码
    private String image;

    private String macAddress;

    private Timestamp time;

}
