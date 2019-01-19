package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.TaskMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public boolean insertTask(Task task) {
        return taskMapper.insertTask(task);
    }

    @Override
    public boolean deleteTaskById(Integer uid,Integer tid) {
        return taskMapper.deleteTaskById(uid,tid);
    }

    @Override
    public boolean alterTask(Task task) {
        return taskMapper.alterTask(task);
    }

    @Override
    public Task queryTask(Integer uid,Integer tid) {
        return taskMapper.queryTask(uid,tid);
    }

    @Override
    public boolean completeTask(Integer uid, Integer tid) {
        return taskMapper.completeTask(uid,tid);
    }

    @Override
    public Task[] queryTasksByUid(Integer uid) {
        return taskMapper.queryTasksByUid(uid);
    }
}
