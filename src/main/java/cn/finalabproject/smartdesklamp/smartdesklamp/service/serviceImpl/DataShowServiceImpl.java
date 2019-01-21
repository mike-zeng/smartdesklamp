package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.SittingPostureMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.DataShowService;
import cn.finalabproject.smartdesklamp.smartdesklamp.vo.BaseDataViewObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataShowServiceImpl implements DataShowService {
    @Autowired
    SittingPostureMapper sittingPostureMapper;

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
}
