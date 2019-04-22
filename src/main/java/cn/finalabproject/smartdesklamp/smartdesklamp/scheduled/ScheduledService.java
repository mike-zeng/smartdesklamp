package cn.finalabproject.smartdesklamp.smartdesklamp.scheduled;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.DataStatisticsMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.*;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.SittingPostureService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.UserService;
import cn.finalabproject.smartdesklamp.smartdesklamp.utils.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class ScheduledService {
    private static Integer[] userIdArray=null;
    FocusStatistics focusStatistics=null;
    PostureScoreStatistics postureScoreStatistics=null;
    StudyTimeStatistics studyTimeStatistics=null;
    SittingPostureStatistics sittingPostureStatistics=null;

    @Autowired
    UserService userService;
    @Autowired
    DataStatisticsMapper dataStatisticsMapper;
    @Autowired
    SittingPostureService sittingPostureService;

    SittingPostureInfo[] list=null;
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduled(){
        System.out.println("定时任务开始执行"+new Date());
        //获取用户id列表
        userIdArray=userService.getAllUserIdList();
        for (Integer uid:userIdArray){
            focusStatistics=getFocusStatistics(uid);
            postureScoreStatistics=getPostureScoreStatistics(uid);
            studyTimeStatistics=getStudyTimeStatistics(uid);
            sittingPostureStatistics=getSittingPostureStatistics(uid);
            //写入数据库
            dataStatisticsMapper.addFocusStatistics(focusStatistics);
            dataStatisticsMapper.addPostureScoreStatistics(postureScoreStatistics);
            dataStatisticsMapper.addStudyTimeStatistics(studyTimeStatistics);
            dataStatisticsMapper.addSittingPostureStatistics(sittingPostureStatistics);
            System.out.println("成功写入数据");
        }
    }
    //根据坐姿的方差和平均值判断
    public  FocusStatistics getFocusStatistics(Integer uid){
        FocusStatistics focusStatistics=new FocusStatistics();
        //获取数据。。。
        SittingPostureInfo[] list=sittingPostureService.getPosturesByDate(new Date(),uid);
        Double score=CalculateUtil.calculateFoucsScoreByPostures(list);
        focusStatistics.setFocusScore(score);
        focusStatistics.setUid(uid);
        return focusStatistics;
    }
    //获取坐姿分数 ok
    public  PostureScoreStatistics getPostureScoreStatistics(int uid){
        PostureScoreStatistics postureScoreStatistics=new PostureScoreStatistics();
        SittingPostureInfo[] list=sittingPostureService.getPosturesByDate(new Date(),uid);
        double score=CalculateUtil.calculateScoreByPostures(list);
        postureScoreStatistics.setScore(score);
        postureScoreStatistics.setUid(uid);
        return postureScoreStatistics;
    }
    //获取学习时间长。ok
    public  StudyTimeStatistics getStudyTimeStatistics(int uid){
        StudyTimeStatistics studyTimeStatistics=new StudyTimeStatistics();
        int count=sittingPostureService.getCountByDate(new Date());
        studyTimeStatistics.setTotalTime(count*2);
        studyTimeStatistics.setUid(uid);
        return studyTimeStatistics;
    }
    //获取坐姿数据，每一天，各种数据的统计 ok
    public SittingPostureStatistics getSittingPostureStatistics(int uid){
        SittingPostureStatistics sittingPostureStatistics=new SittingPostureStatistics();
        //获取数据。。。
        SittingPostureInfo[] list=sittingPostureService.getPosturesByDate(new Date(),uid);
        int[] book=new int[7];
        for (SittingPostureInfo s:list){
            book[s.getStatus()]++;
        }
        sittingPostureStatistics.setT0(book[0]);
        sittingPostureStatistics.setT1(book[1]);
        sittingPostureStatistics.setT2(book[2]);
        sittingPostureStatistics.setT3(book[3]);
        sittingPostureStatistics.setT4(book[4]);
        sittingPostureStatistics.setT5(book[5]);
        sittingPostureStatistics.setT6(book[6]);
        sittingPostureStatistics.setUid(uid);
        return sittingPostureStatistics;
    }
}
