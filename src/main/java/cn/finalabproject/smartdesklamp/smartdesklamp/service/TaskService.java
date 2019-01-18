package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;

public interface TaskService {
    public boolean insertTask(Task task);

    public boolean deleteTaskById(Integer id);

    public boolean alterTask(Task task);

    public Task queryTask(Integer id);

    public Task[] queryTasksByUid(Integer uid);
}
