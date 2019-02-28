package cn.finalabproject.smartdesklamp.smartdesklamp.mapper;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.FocusStatistics;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.PostureScoreStatistics;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.SittingPostureStatistics;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.StudyTimeStatistics;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * 用来查询统计数据
 */
@Mapper
public interface DataStatisticsMapper {

    @Select("select * from sitting_posture_statistics where uid=#{uid} and to_days(time)=to_days(#{date})")
    public SittingPostureStatistics querySittingPostureStatistics(@Param("uid") int uid, @Param("date") Date date);

    @Select("select * from study_time_statistics where uid=#{uid} and week(time)=week(#{date})")
    @Results({
            @Result(property = "totalTime",column = "total_time")
    })
    public StudyTimeStatistics[] queryStudyTimeStatistics(@Param("uid") int uid, @Param("date") Date date);

    @Select("select * from posture_score_statistics where uid=#{uid} and week(time)=week(#{date})")
    public PostureScoreStatistics[] queryPostureScoreStatistics(@Param("uid") int uid, @Param("date") Date date);

    @Select("select * from focus_statistics where uid=#{uid} and week(time)=week(#{date})")
    @Results({
            @Result(property = "focusScore",column = "focus_score")
    })
    public FocusStatistics[] queryFoucsStatistics(@Param("uid") int uid, @Param("date") Date date);

    @Insert("insert into sitting_posture_statistics(uid,t0,t1,t2,t3,t4,t5,t6) values(#{uid},#{t0},#{t1},#{t2},#{t3},#{t4},#{t5},#{t6})")
    public boolean addSittingPostureStatistics(SittingPostureStatistics obj);

    @Insert("insert into study_time_statistics(uid,total_time) values(#{uid},#{totalTime})")
    public boolean addStudyTimeStatistics(StudyTimeStatistics obj);

    @Insert("insert into posture_score_statistics(uid,score) values(#{uid},#{score})")
    public boolean addPostureScoreStatistics(PostureScoreStatistics obj);

    @Insert("insert into focus_statistics(uid,focus_score) values(#{uid},#{focusScore})")
    public boolean addFocusStatistics(FocusStatistics obj);


}
