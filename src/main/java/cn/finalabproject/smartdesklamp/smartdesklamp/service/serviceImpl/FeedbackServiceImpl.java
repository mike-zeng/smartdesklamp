package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Feedback;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureInfo;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.FeedbackService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SittingPostureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    SittingPostureService sittingPostureService;
    @Override
    public Feedback getFeedBack(int uid, Date startDate, Date endDate) {
        long totalTime=(endDate.getTime()-startDate.getTime())/(1000*60);
        SittingPostureInfo[] infos=sittingPostureService.queryPostures(new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime()),uid);
        Feedback feedback=new Feedback();
        int[] book=new int[7];
        double[] degree=new double[7];
        for (int i = 0; i < infos.length; i++) {
            book[infos[i].getStatus()]++;
            degree[infos[i].getStatus()]+=infos[i].getDegree();
        }

        for (int i = 0; i < 7; i++) {
            if (book[i]!=0){
                degree[i]=degree[i]/book[i];
            }
        }
        degree[0]=100;
        //錯誤次數
        feedback.setWrongCount(infos.length-book[0]);
        //效率
        double effectiveness=100;
        for (int i = 1; i < 7; i++) {
            effectiveness-=degree[i]*((double) book[i]/infos.length);
        }
        feedback.setEffectiveness(effectiveness);
        //最平凡的錯誤姿勢
        int frequent=Integer.MIN_VALUE;
        for (int i = 1; i < 7; i++) {
            if (book[i]>frequent){
                frequent=i;
            }
        }
        feedback.setFrequent(frequent);
        //有效時間
        feedback.setEffectiveTime((int) ((effectiveness/100)*totalTime));
        return feedback;
    }
}
