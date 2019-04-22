package cn.finalabproject.smartdesklamp.smartdesklamp.spd.spd;

import cn.finalabproject.smartdesklamp.smartdesklamp.spd.detector.impl.HeadDetector;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.detector.impl.UpperBodyDetector;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.HeadInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.SittingPosition;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.SpdImage;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.model.UpperBodyInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.service.SittingPositionDetection;
import cn.finalabproject.smartdesklamp.smartdesklamp.spd.service.impl.SimpleSittingPositionDetection;
import java.util.HashMap;

/**
 * spd是该包的入口
 */
public class Spd {
    private static Spd spd=new Spd();
    private static HeadDetector headDetector=null;
    private static UpperBodyDetector upperBodyDetector=null;

   private Spd(){
   }
   //单例模式，谁让baidu Api免费用户不支持并发呢
   public static  Spd getInstance(HashMap<String,String> headDetectorConfig,HashMap<String,String> upperBodyDetectorConfig){
       //获取单例
       headDetector=HeadDetector.getHeadDetecto();
       upperBodyDetector=UpperBodyDetector.getUpperBodyDetector();

       boolean b1=headDetector.init(headDetectorConfig.get("appId"),headDetectorConfig.get("apiKey"),headDetectorConfig.get("secretKey"));
       boolean b2=upperBodyDetector.init(upperBodyDetectorConfig.get("appId"),upperBodyDetectorConfig.get("apiKey"),upperBodyDetectorConfig.get("secretKey"));
       if (!(b1&&b2)){
           return null;
       }
       return spd;
   }

    /**
     * 获取坐姿信息
     * @param uid 用户id
     * @param base 图片的base64码，大小不超过2MB
     * @return 坐姿信息类
     */
   public SittingPosition getSittingPosition(Integer uid, String base){
       HeadInfo headInfo=null;
       UpperBodyInfo upperBodyInfo=null;

       SpdImage spdImage=new SpdImage(base);
       //获取基础信息：头部信息，关键点信息
       try {
           headInfo=headDetector.detection(spdImage);
           upperBodyInfo=upperBodyDetector.detection(spdImage);

           if (headInfo==null&&upperBodyInfo==null){
               return null;
           }
       }catch (Exception e){
           e.printStackTrace();
       }

       SittingPosition sittingPosition=null;

       SittingPositionDetection sittingPositionDetection=new SimpleSittingPositionDetection();
       sittingPosition=sittingPositionDetection.getSittingPosition(headInfo,upperBodyInfo);
       sittingPosition.setUid(uid);
       return sittingPosition;
   }
}
