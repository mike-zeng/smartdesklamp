package cn.finalabproject.smartdesklamp.smartdesklamp.service.serviceImpl;

import cn.finalabproject.smartdesklamp.smartdesklamp.mapper.TaskMapper;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public boolean insertTask(Task task) {
        return taskMapper.insertTask(task);
    }

    @Override
    public boolean deleteTaskById(Integer id) {
        return taskMapper.deleteTaskById(id);
    }

    @Override
    public boolean alterTask(Task task) {
        return taskMapper.alterTask(task);
    }

    @Override
    public Task queryTask(Integer id) {
        return taskMapper.queryTask(id);
    }

    @Override
    public Task[] queryTasksByUid(Integer uid) {
        return taskMapper.queryTasksByUid(uid);
    }
}
