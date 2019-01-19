package cn.finalabproject.smartdesklamp.smartdesklamp;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.TomatoClock;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TaskService;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TomatoClockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmartdesklampApplicationTests {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TomatoClockService tomatoClockService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void Test(){
        TomatoClock tomatoClock = new TomatoClock();
        tomatoClock.setCreatTime(new Timestamp(System.currentTimeMillis()));
        tomatoClock.setDuration(25);
        tomatoClock.setIncorrectPostureNum(11);
        tomatoClock.setScore(35);
        tomatoClock.setTaskId(1);
        tomatoClock.setWarningTimes(15);
        tomatoClock.setId(1);
        tomatoClockService.deleteTomatoClock(2);
//        tomatoClockService.updateTomatoClock(tomatoClock);
//        tomatoClockService.creatTomatoClock(tomatoClock);
//        System.out.println(tomatoClock.getId());
//        System.out.println(tomatoClockService.queryTomatoClocksByTaskId(1)[0]);
    }

}

