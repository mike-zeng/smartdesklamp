package cn.finalabproject.smartdesklamp.smartdesklamp.model;

//通过注入的方式获取不需要被拦截的URI

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "my.config")
public class ExcludeURI {
    private List<String> excludeuri=new LinkedList<>();

    public List<String> getExcludeuri() {
        return excludeuri;
    }

    public void setExcludeuri(List<String> excludeuri) {
        this.excludeuri = excludeuri;
    }
}
