package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;

public interface TaskService {
    public boolean insertTask(Task task);

    public boolean deleteTaskById(Integer uid,Integer tid);

    public boolean alterTask(Task task);

    public Task queryTask(Integer uid,Integer tid);

    public boolean completeTask(Integer uid,Integer tid);

    public Task[] queryTasksByUid(Integer uid);
}
