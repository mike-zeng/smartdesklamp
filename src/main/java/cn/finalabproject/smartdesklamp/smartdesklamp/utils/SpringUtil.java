package cn.finalabproject.smartdesklamp.smartdesklamp.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;


public class SpringUtil{
    private static ApplicationContext applicationContext=null;


    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext==null){
            SpringUtil.applicationContext=applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;

    }

    public static Object getBean(String name) {

        return getApplicationContext().getBean(name);

    }

    public static <T> T getBean(Class<T> clazz) {

        return getApplicationContext().getBean(clazz);

    }

    public static <T> T getBean(String name,Class<T> clazz) {
        return getApplicationContext().getBean(name,clazz);
    }

}
