package cn.finalabproject.smartdesklamp.smartdesklamp;

import cn.finalabproject.smartdesklamp.smartdesklamp.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SmartdesklampApplication {

    public static void main(String[] args) {
        ApplicationContext app=SpringApplication.run(SmartdesklampApplication.class, args);
        SpringUtil.setApplicationContext(app);
    }

}

