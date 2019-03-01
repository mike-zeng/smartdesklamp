package cn.finalabproject.smartdesklamp.smartdesklamp.WebSocket;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.SittingPostureDetection;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EnvironmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EquipmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.SittingPostureServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.UserServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.JwtUtils;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.Md5Utils;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
@ServerEndpoint("/getMessageServer/{token}/{eid}/{macAddress}")
public class MessageSocketServer {

    @Autowired
    RedisService redisService;

    private static HashMap<Integer,MessageSocketServer> webSocketMap = new HashMap<>();
    private static HashMap<Integer,MessageSocketServer> webSocketHardwareMap = new HashMap<>();
    private static HashMap<Integer,List<String>> waitToSent=new HashMap<>();
    private static HashMap<Integer,List<String>> HardwareWaitToSent=new HashMap<>();

    private Session session;
    private Integer id;
    private UserInfo userInfo;
    private Integer eid;
    private Integer uid;

    public static void sentToHardWare(Integer eid,String message) {
        MessageSocketServer messageSocketServer = webSocketHardwareMap.get(eid);
        if (messageSocketServer == null) {
            List<String> list = waitToSent.get(eid);
            if (list==null){
                waitToSent.put(eid,new LinkedList<>());
            }else {
                list.add(message);
            }
        }else{
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }
    //接受新的连接，并判断权限是否足够
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token,@PathParam("eid")Integer eid,@PathParam("macAddress")String macAddress) throws IOException {
        this.session = session;
        List<String> list = null;
        UserService userService = SpringUtil.getBean(UserServiceImpl.class);
        //如果是台灯进行连接
        if (token.endsWith("null")) {
            this.eid = eid;
//            EquipmentService equipmentService = SpringUtil.getBean(EquipmentServiceImpl.class);
//            String encriptString = Md5Utils.MD5Encode(macAddress, "utf-8", false);
//            String tempMacAddress = equipmentService.queryEquipmentById(eid).getMacAddress();
            //检测传过来的mac地址是否正确
//            if (tempMacAddress == null || !encriptString.endsWith(tempMacAddress)) {
//                return;
//            } else {
                this.eid = eid;
                this.uid = userService.getUserIdByEid(eid);
                webSocketHardwareMap.put(eid, this);
                //判断有没有该台灯的信息，如果有就发送
                list = HardwareWaitToSent.get(id);//获取信息
//            }
        }else{
            Integer id = JwtUtils.getId(token);
            if(id != null){
                this.id = id;
                webSocketMap.put(id, this);//有新的连接，加入map中
                //判断有没有该用户的信息，如果有就发送
                list = waitToSent.get(id);//获取信息
            }else {
                session.close();
            }
        }
        String message;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                message = list.get(i);
                if (message != null) {
                    session.getAsyncRemote().sendText(message);
                }
                list.remove(i);
            }
        }
        System.out.println("有新的连接" + id);
    }

    @OnClose
    public void onClose() throws IOException {
        webSocketMap.remove(this.id);//连接断开，移除session
        this.session.close();
        System.out.println("一个连接断开"+this.id);
    }

    //接受来自客户端的消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        EquipmentService equipmentService = SpringUtil.getBean(EquipmentServiceImpl.class);
        EnvironmentService environmentService = SpringUtil.getBean(EnvironmentServiceImpl.class);
        SittingPostureService sittingPostureService = SpringUtil.getBean(SittingPostureServiceImpl.class);
        Base64.Decoder decoder = Base64.getDecoder();
        Message m = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            m = objectMapper.readValue(message, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if(m.getType().equals(Message.EQUIPMENT)){
            EquipmentMessage equipmentMessage = m.getEquipmentMessage();
            Integer eid = equipmentMessage.getEquipmentId();
            String encryptString = Md5Utils.MD5Encode(equipmentMessage.getMacAddress(),"utf-8",false);
            //检测传过来的mac地址是否正确
            if(!encryptString.endsWith(equipmentService.queryEquipmentById(eid).getMacAddress())) {
                return;
            }
            Environment environment = new Environment(null,equipmentMessage.getBrightness(),equipmentMessage.getNoise(),equipmentMessage.getTemperature(),equipmentMessage.getHumidity(),equipmentMessage.getTime(),equipmentMessage.getEquipmentId());
            environmentService.insertEnvironment(environment);
            //从消息中读取Base64加密后的字符串
            String image = equipmentMessage.getImage();
//            byte[] bytes = decoder.decode(image);
//            InputStream buffin = new ByteArrayInputStream(bytes,0,bytes.length);
//            BufferedImage img = ImageIO.read(buffin);
            SittingPostureInfo sittingPostureInfo = SittingPostureDetection.getSittingPosttureInfo(uid,image);
            sittingPostureInfo.setUid(id);
            sittingPostureInfo.setTime(equipmentMessage.getTime());
//            sittingPostureService.insertPosture(sittingPostureInfo);
        }

    }

    //发送消息
    public void sendMessage(Integer id,String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}

/**
 * 定义一条消息
 */
@Setter
@Getter
@AllArgsConstructor
class Message{
    public static final String EQUIPMENT="equipment";
    public static final String USER="user";

    public Message() {

    }

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
