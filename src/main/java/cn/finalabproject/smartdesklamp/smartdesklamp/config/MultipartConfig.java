package cn.finalabproject.smartdesklamp.smartdesklamp.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.of(5,DataUnit.MEGABYTES));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(10,DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
