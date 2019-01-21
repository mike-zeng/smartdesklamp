package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EnvironmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EquipmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.SittingPostureMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.DataShowService;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.BaseDataViewObject;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.EnvironmentInfoViewObject;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.SpecificEnvironmentDataViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataShowServiceImpl implements DataShowService {
    @Autowired
    SittingPostureMapper sittingPostureMapper;
    @Autowired
    EnvironmentMapper environmentMapper;
    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public BaseDataViewObject getBaseData(Integer uid) {
        BaseDataViewObject baseDataViewObject=new BaseDataViewObject();
        Date date=new Date();
        Timestamp begin=new Timestamp(date.getTime()-1000*60*60);
        Timestamp end=new Timestamp(date.getTime());

        //获取当前得分，从当前时间向前推60分钟
        SittingPostureInfo[] sittingPostureInfos=sittingPostureMapper.queryPostures(begin,end,uid);
        int[] book=new int[7];
        int flag=-1;
        double score=0;
        double accuracy=0;
        int false_p=0,true_p=0;

        for (int i=0;i<sittingPostureInfos.length;i++){
            book[sittingPostureInfos[i].getStatus()]++;//统计次数
        }

        //获取统计数据
        Map<Integer,Integer> map=new HashMap<>();
        for (int i=0;i<7;i++){
            if (i==0){
                true_p=book[i];
            }else {
                false_p+=book[i];
            }
            map.put(i,book[i]);
        }

        int count=sittingPostureMapper.getCountByDate(new Date());

        //计算分数,最简陋的一种方式,毫无科学依据
        score=book[0]*10+book[1]*7+book[2]*7+book[3]*5+book[4]*5+book[5]*3;
        score=100-(100*(book[1]+book[2])/count*0.7+(100*(book[3]+book[4])/count*0.8)+100*book[5]/count*0.9+100*book[6]/count*1);

        //获取总时间长
        accuracy=((double)true_p)/(false_p+true_p);

        //填充数据
        baseDataViewObject.setTotalTime(count*2);//总时长 ok
        baseDataViewObject.setSittingPostureStatistics(map);//统计 ok
        baseDataViewObject.setScore(score);//得分
        baseDataViewObject.setGrade((int)score%20+1);//等级
        baseDataViewObject.setAccuracy(accuracy);//正确率

        //返回数据
        return baseDataViewObject;
    }

    @Override
    public EnvironmentInfoViewObject getEnvironmentData(Integer eid) {
        Integer workTime = null;
        EnvironmentInfoViewObject environmentInfoViewObject = new EnvironmentInfoViewObject();
        Environment environment = environmentMapper.queryCurrentEnvironmentInfo(eid);
        Integer musicId = equipmentMapper.getCurrentMusicId(eid);
        Integer count = sittingPostureMapper.getCountByDate(new Date());
        if(count == null){
            workTime = 0;
        }else{
            workTime = count * 2;
        }
        if(environment != null){
            environmentInfoViewObject.setBrightness(environment.getBrightness());
            environmentInfoViewObject.setHumidity(environment.getHumidity());
            environmentInfoViewObject.setNoise(environment.getNoise());
            environmentInfoViewObject.setTemperature(environment.getTemperature());
        }
        environmentInfoViewObject.setWorkTime(workTime);
        environmentInfoViewObject.setMusicId(musicId);
        return environmentInfoViewObject;
    }

    @Override
    public SpecificEnvironmentDataViewObject getSpecificData(Date date,Integer eid){
        Float[] temperature = new Float[24];
        Float[] humidity = new Float[24];
        Float[] noise = new Float[24];
        Float[] brightness = new Float[24];
        SpecificEnvironmentDataViewObject specificEnvironmentDataViewObject = new SpecificEnvironmentDataViewObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(date);
        //从0时到当前的时刻，求出每个时刻的平均值
        for(int i = 0;i < currentHour;i++){
            brightness[i] = 0f;
            humidity[i] = 0f;
            noise[i] = 0f;
            temperature[i] = 0f;
            Timestamp beginDate = new Timestamp(calendar.getTime().getTime());
            calendar.add(Calendar.HOUR,1);
            Timestamp endDate = new Timestamp(calendar.getTime().getTime());
            Environment[] environments = environmentMapper.queryEnvironmentsBtTime(beginDate,endDate,eid);
            if(environments != null){
                for(Integer j = 0;j < environments.length;j++){
                    brightness[i] += environments[j].getBrightness();
                    humidity[i] += environments[j].getHumidity();
                    noise[i] += environments[i].getNoise();
                    temperature[i] += environments[i].getTemperature();
                }
                brightness[i] /= environments.length;
                humidity[i] /= environments.length;
                noise[i] /= environments.length;
                temperature[i] /= environments.length;
            }
            specificEnvironmentDataViewObject.setBrightness(brightness);
            specificEnvironmentDataViewObject.setHumidity(humidity);
            specificEnvironmentDataViewObject.setNoise(noise);
            specificEnvironmentDataViewObject.setTemperature(temperature);
        }
        return specificEnvironmentDataViewObject;
    }
}
