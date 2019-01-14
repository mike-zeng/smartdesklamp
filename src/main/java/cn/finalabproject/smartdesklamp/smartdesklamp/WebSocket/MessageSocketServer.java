package cn.finalabproject.smartdesklamp.smartdesklamp.WebSocket;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.SittingPostureDetection;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.UserInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EnvironmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.RedisService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SittingPostureService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EnvironmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EquipmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.SittingPostureServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.JwtUtils;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.Md5Utils;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@ServerEndpoint("/getMessageServer/{token}")
@Component
public class MessageSocketServer {

    @Autowired
    RedisService redisService;

    private static HashMap<Integer,MessageSocketServer> webSocketMap = new HashMap<>();
    private static HashMap<Integer,List<String>> waitToSent=new HashMap<>();

    private Session session;
    private Integer id;
    private UserInfo userInfo;

    //发送给所有的用户
    public static void sentAll(String message){
        Set<Integer> set=webSocketMap.keySet();
        MessageSocketServer messageSocketServer=null;
        for (Integer it:set){
            messageSocketServer=webSocketMap.get(it);
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }

    //发送给指定的用户集合
    public static void sentAll(Integer[] idArr,String message){
        Session session=null;
        MessageSocketServer messageSocketServer=null;
        for (int i=0;i<idArr.length;i++){
            messageSocketServer=webSocketMap.get(idArr[i]);
            if (messageSocketServer==null){
                //如果当前用户不在线，则放在缓存列表中。。。。。。
                List<String> list=waitToSent.get(idArr[i]);
                if (list==null){
                    waitToSent.put(idArr[i],new LinkedList<>());
                }else {
                    list.add(message);
                }
                continue;
            }
            messageSocketServer.session.getAsyncRemote().sendText(message);
        }
    }


    //接受新的连接，并判断权限是否足够
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) throws IOException {
        this.session=session;
        Integer id= JwtUtils.getId(token);
        if (id!=null){
            this.id=id;
            webSocketMap.put(id,this);//有新的连接，加入map中
            //判断有没有该用户的信息，如果有就发送
            List<String> list=waitToSent.get(id);//获取信息
            String message;
            if (list!=null){
                for (int i=0;i<list.size();i++){
                    message=list.get(i);
                    if (message!=null){
                        session.getAsyncRemote().sendText(message);
                    }
                    list.remove(i);
                }
            }
            System.out.println("有新的连接"+id);
        }else {
            session.close();
        }
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
        Message m=null;

        ObjectMapper objectMapper=new ObjectMapper();
        try {
            m=objectMapper.readValue(message,Message.class);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }


        Integer eid = m.getEquipmentId();
        String encriptString = Md5Utils.MD5Encode(m.getMacAddress(),"utf-8",false);
        //检测传过来的mac地址是否正确
        if(!encriptString.endsWith(equipmentService.queryEquipmentById(eid).getMacAddress())) {
            return;
        }
        Environment environment = new Environment(null,m.getBrightness(),m.getNoise(),m.getTemperature(),m.getHumidity(),m.getTime(),m.getEquipmentId());
        environmentService.insertEnvironment(environment);
        //从消息中读取Base64加密后的字符串
        String image = m.getImage();
        byte[] bytes = decoder.decode(image);
        InputStream buffin = new ByteArrayInputStream(bytes,0,bytes.length);
        BufferedImage img = ImageIO.read(buffin);
        SittingPostureInfo sittingPostureInfo = SittingPostureDetection.getSittingPosttureInfo(img);
        sittingPostureInfo.setUid(id);
        sittingPostureInfo.setTime(m.getTime());
        sittingPostureService.insertPosture(sittingPostureInfo);
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
    public Message() {
    }
    private Integer equipmentId;

    private float brightness;

    private float noise;

    private float temperature;

    private float humidity;
    //String str = Base64.encodeToString(mBuff,Base64.DEFAULT);将图片用base64编码
    private String image;

    private String macAddress;

    private Timestamp time;

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
