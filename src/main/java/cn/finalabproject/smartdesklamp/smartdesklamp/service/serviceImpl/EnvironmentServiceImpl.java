package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.EnvironmentMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Environment;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {
    @Autowired
    private EnvironmentMapper environmentMapper;
    @Override
    public int insertEnvironment(Environment environment) {
        return environmentMapper.insertEnvironment(environment);
    }

    @Override
    public boolean updateEnvironment(Environment environment) {
        return environmentMapper.updateEnvironment(environment);
    }

    @Override
    public boolean deleteEnvironment(Integer id) {
        return environmentMapper.deleteEnvironment(id);
    }

    @Override
    public Environment[] queryEnvironmentsByTime(Timestamp lowTime, Timestamp highTime,Integer eid) {
        return environmentMapper.queryEnvironmentsBtTime(lowTime,highTime,eid);
    }

    @Override
    public Environment queryEnvironmentById(Integer id) {
        return environmentMapper.queryEnvironmentById(id);
    }

    @Override
    public Environment[] queryCurrentEnvironmentInfo(Integer eid) {
        return environmentMapper.queryCurrentEnvironmentInfo(eid);
    }
}
