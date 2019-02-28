package cn.finalabproject.smartdesklamp.smartdesklamp.common;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import model.SittingPosition;
import spd.Spd;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * 根据传进来的图片，进行分析，获得坐姿信息，可能会失败，如果检测失败则返回null
 */
public class SittingPostureDetection {
    private static  HashMap<String,String> config1,config2;
    static {
        config1 = new HashMap<String, String>();
        config1.put("appId","15524921");
        config1.put("apiKey","xo257fvmCmgEvFbhvR16SBMQ");
        config1.put("secretKey","yNuCjyk7Ay9x8vLLgElqLMWqgZa94hom");
        config2=new HashMap<String, String>();
        config2.put("appId","15525268");
        config2.put("apiKey","nuG1QaBrDYg71PgFZyyWrFKw");
        config2.put("secretKey","o9YGs8GtmBg1oGrL1buAmiQDWDjRgXCe");
    }

    public static SittingPostureInfo getSittingPosttureInfo(Integer uid,String image){
        Spd spd = Spd.getInstance(config1,config2);

        SittingPosition sittingPosition = spd.getSittingPosition(uid,image);
        return new SittingPostureInfo(sittingPosition.getId(),sittingPosition.getUid(),sittingPosition.getStatus(),100,new Timestamp(new Date().getTime()));
    }
}
