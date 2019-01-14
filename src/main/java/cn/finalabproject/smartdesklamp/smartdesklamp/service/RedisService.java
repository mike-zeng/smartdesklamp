package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import java.util.Set;

public interface RedisService {

    public void set(String key, String value);


    public void set(String key, String value, long expireTime);

    public void expire(String key, long time);


    public void hset(String hash, String key, String value);

    public String hget(String hash, String key);

    public void hdel(String hash, String key);


    public boolean exists(String key) ;

    public Boolean remove(String key);


    public Object get(String key) ;

    public void sadd(String key, String... arr);

    public void setbit(String key, long pos, boolean flag);

    public Set<String> sget(String key);
}
