package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.DataStatisticsMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EnvironmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EquipmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.SittingPostureMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.DataShowService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.CalculateUtil;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.*;
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
    @Autowired
    DataStatisticsMapper dataStatisticsMapper;

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
        if (count!=0){
            score=100-(100*(book[1]+book[2])/count*0.7+(100*(book[3]+book[4])/count*0.8)+100*book[5]/count*0.9+100*book[6]/count*1);
        }else{
            score=0;
        }

        if (count!=0){
            accuracy=((double)true_p)/(false_p+true_p);
        }

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
        Environment[] environments = environmentMapper.queryCurrentEnvironmentInfo(eid);
        Integer musicId = equipmentMapper.getCurrentMusicId(eid);
        Integer count = sittingPostureMapper.getCountByDate(new Date());
        if(count == null){
            workTime = 0;
        }else{
            workTime = count * 2;
        }
        if(environments != null){
            if(environments.length == 2){
                environmentInfoViewObject.setBrightnessChange(environments[0].getBrightness() - environments[1].getBrightness());
                environmentInfoViewObject.setHumidityChange(environments[0].getHumidity() - environments[1].getHumidity());
                environmentInfoViewObject.setTemperatureChange(environments[0].getTemperature() - environments[1].getTemperature());
                environmentInfoViewObject.setNoiseChange(environments[0].getNoise() - environments[1].getNoise());
            }else{
                environmentInfoViewObject.setBrightnessChange(0);
                environmentInfoViewObject.setHumidityChange(0);
                environmentInfoViewObject.setTemperatureChange(0);
                environmentInfoViewObject.setNoiseChange(0);
            }
            environmentInfoViewObject.setBrightness(environments[0].getBrightness());
            environmentInfoViewObject.setHumidity(environments[0].getHumidity());
            environmentInfoViewObject.setNoise(environments[0].getNoise());
            environmentInfoViewObject.setTemperature(environments[0].getTemperature());
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

    @Override
    public SittingPostureViewObject getSittingPostureData(Integer uid, Date date) {
        SittingPostureStatistics data=dataStatisticsMapper.querySittingPostureStatistics(uid,date);
        if (data==null){
            data=new SittingPostureStatistics();
        }
        Map<Integer,Integer> map=new HashMap<>();
        int[] book=new int[7];
        book[0]=data.getT0();
        book[1]=data.getT1();
        book[2]=data.getT2();
        book[3]=data.getT3();
        book[4]=data.getT4();
        book[5]=data.getT5();
        book[6]=data.getT6();

        map= CalculateUtil.getPosturesMap(book);
        double accuracy=CalculateUtil.getAccuracy(book);
        double score=CalculateUtil.calculateScoreByPostures(book);
        int grade=CalculateUtil.getGrade(score);
        SittingPostureViewObject vo=new SittingPostureViewObject(map,accuracy,score,grade);
        return vo;
    }

    @Override
    public StudyTimeViewObject getStudyTimeData(Integer uid, Date date) {

        StudyTimeStatistics[] data=dataStatisticsMapper.queryStudyTimeStatistics(uid,date);

        int[] time=new int[7];
        int sum=0;
        for (int i=0;i<data.length;i++){
            time[i]=data[i].getTotalTime();
            sum+=data[i].getTotalTime();
        }

        int grade=sum/150+1;
        if (grade>10){
            grade=10;
        }
        StudyTimeViewObject vo=new StudyTimeViewObject(time,sum,sum/7.0,grade);
        return vo;
    }

    @Override
    public MarkDataViewObject getMarkData(Integer uid,Date date) {
        PostureScoreStatistics[] data=dataStatisticsMapper.queryPostureScoreStatistics(uid,date);
        double[] scores=new double[7];
        double sum=0;
        for (int i=0;i<data.length;i++){
            scores[i]=data[i].getScore();
            sum+=scores[i];
        }
        double variance=CalculateUtil.getVariance(scores);
        int grade=0;
        MarkDataViewObject vo=new MarkDataViewObject(scores,sum/7,variance,grade);
        return vo;
    }

    @Override
    public FocusViewObject getFocusData(Integer uid,Date date) {
        FocusStatistics[] data=dataStatisticsMapper.queryFoucsStatistics(uid,date);
        double[] scores=new double[7];
        double sum=0;
        for (int i=0;i<data.length;i++){
            scores[i]=data[i].getFocusScore();
            sum+=scores[i];
        }

        double average=sum/7;
        double variance=0;
        int grade=0;

        FocusViewObject vo=new FocusViewObject(scores,average,variance,grade);
        return vo;
    }
}
