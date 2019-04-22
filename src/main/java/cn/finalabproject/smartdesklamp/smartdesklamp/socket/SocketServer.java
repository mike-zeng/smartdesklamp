package cn.finalabproject.smartdesklamp.smartdesklamp.socket;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.SittingPostureDetection;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.EquipmentMessage;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Message;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EnvironmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EquipmentService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SittingPostureService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EnvironmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.EquipmentServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.SittingPostureServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl.UserServiceImpl;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer
{
    private static Map<String, Socket> socketMap;

    public SocketServer() {
        Socket client = null;
        try {
            socketMap = new HashMap<>();
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("你的ip为" + serverSocket.getInetAddress().getHostAddress());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            while (true) {
                client = serverSocket.accept();
                String ip = client.getInetAddress().getHostAddress();
                System.out.println("ip:" + ip);
                socketMap.put(ip, client);
                executorService.execute(new Thread(ip));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    class Thread implements Runnable{
        private String ip;

        public Thread(String ip){
            this.ip = ip;
        }

        @Override
        public void run()
        {
            EquipmentService equipmentService = SpringUtil.getBean(EquipmentServiceImpl.class);
            EnvironmentService environmentService = SpringUtil.getBean(EnvironmentServiceImpl.class);
            SittingPostureService sittingPostureService = SpringUtil.getBean(SittingPostureServiceImpl.class);
            UserService userService = SpringUtil.getBean(UserServiceImpl.class);
            Base64.Decoder decoder = Base64.getDecoder();
            Message m = null;
            Socket client = null;
            InputStream ins = null;
            OutputStream ous = null;
            BufferedReader br = null;
            String message = null;
            try
            {
                client = socketMap.get(ip);
                ins = client.getInputStream();
                br = new BufferedReader(new InputStreamReader(ins));
                Integer uid = null;
                while (true) {
                    while(ins.available() > 0) {
                        byte[] data = new byte[ins.available()];
                        ins.read(data);
                        message = new String(data);
                        //对于python特殊处理！！！
                        message = message.replace('\'','\"');
                        message = message.replaceAll("None","null");

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
                            //String encryptString = Md5Utils.MD5Encode(equipmentMessage.getMacAddress(),"utf-8",false);
                            uid = userService.getUserIdByEid(eid);
//                            检测传过来的mac地址是否正确
//                            if(!encryptString.endsWith(equipmentService.queryEquipmentById(eid).getMacAddress())) {
//                                return;
//                            }
                            Environment environment = new Environment(null,equipmentMessage.getBrightness(),equipmentMessage.getNoise(),equipmentMessage.getTemperature(),equipmentMessage.getHumidity(),equipmentMessage.getTime(),equipmentMessage.getEquipmentId());
                            environmentService.insertEnvironment(environment);
                            //从消息中读取Base64加密后的字符串
                            String image = equipmentMessage.getImage();
                            try {
                                SittingPostureInfo sittingPostureInfo = SittingPostureDetection.getSittingPosttureInfo(uid,image);
                                sittingPostureInfo.setUid(uid);
                                sittingPostureInfo.setTime(equipmentMessage.getTime());
                                sittingPostureService.insertPosture(sittingPostureInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    br.close();
                    ins.close();
                    client.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(String message,String ip){
        Socket socket = SocketServer.socketMap.get(ip);
        OutputStream ous = null;
        if(socket == null){
            return;
        }
        try {
            ous = socket.getOutputStream();
            ous.write(message.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

