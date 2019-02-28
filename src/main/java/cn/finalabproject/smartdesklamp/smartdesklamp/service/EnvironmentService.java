package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;

import java.sql.Timestamp;

public interface EnvironmentService {
    public int insertEnvironment(Environment environment);

    public boolean updateEnvironment(Environment environment);

    public boolean deleteEnvironment(Integer equipmentId);

    public Environment[] queryEnvironmentsByTime(Timestamp lowTime,Timestamp highTime,Integer eid);

    public Environment queryEnvironmentById(Integer equipmentId);

    public Environment[] queryCurrentEnvironmentInfo(Integer eid);

}
