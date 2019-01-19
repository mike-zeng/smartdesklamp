package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EfficiencyController {
    @Autowired
    TaskService taskService;

    /**
     * 添加任务
     * @param task
     * @param request
     * @return
     */
    @RequestMapping("/addTask")
    public RetJson addTask(Task task, HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        task.setUid(uid);
        if (!taskService.insertTask(task)){
            return RetJson.fail(-1,"添加失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 修改任务
     * @param task 任务
     * @param request 请求
     * @return
     */
    @RequestMapping("/alterTask")
    public RetJson alterTask(Task task,HttpServletRequest request){
        if (!taskService.alterTask(task)){
            return RetJson.fail(-1,"修改失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 删除任务
     * @param id 任务id
     * @param request
     * @return
     */
    @RequestMapping("/deleteTask")
    public RetJson deleteTask(Integer id,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        if (!taskService.deleteTaskById(uid,id)){
            return RetJson.fail(-1,"删除失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 获取用户所有的任务
     * @param request
     * @return
     */
    @RequestMapping("/getTasks")
    public RetJson getTasks(HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        Task[] tasks=taskService.queryTasksByUid(uid);
        return RetJson.succcess("tasks",tasks);
    }

    /**
     * 通过id获取任务
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/getTaskById")
    public RetJson getTaskById(Integer id,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        Task task=taskService.queryTask(uid,id);
        if (task==null){
            return RetJson.fail(-1,"获取失败!");
        }
        return RetJson.succcess("task",task);
    }

    /**
     * 完成任务
     * @param id 任务id
     * @param request
     * @return
     */
    @RequestMapping("/completeTask")
    public RetJson completeTask(Integer id,HttpServletRequest request){
        Integer uid=((User)request.getAttribute("user")).getId();
        if (!taskService.completeTask(uid,id)){
            return RetJson.fail(-1,"状态修改失败!");
        }
        return RetJson.succcess(null);
    }


}
