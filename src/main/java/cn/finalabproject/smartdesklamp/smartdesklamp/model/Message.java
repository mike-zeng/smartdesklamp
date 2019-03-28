package cn.finalabproject.smartdesklamp.smartdesklamp.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 定义一条消息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message{
    public static final String EQUIPMENT="equipment";
    public static final String USER="user";
    private String type;

    private EquipmentMessage equipmentMessage;

    private UserMessage userMessage;

    public String toString(){
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "{}";
    }
}