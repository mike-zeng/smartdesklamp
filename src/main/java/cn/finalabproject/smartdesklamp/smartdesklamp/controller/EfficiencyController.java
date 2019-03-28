package cn.finalabproject.smartdesklamp.smartdesklamp.controller;

import cn.finalabproject.smartdesklamp.smartdesklamp.common.RetJson;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.Task;
import cn.finalabproject.smartdesklamp.smartdesklamp.model.User;
import cn.finalabproject.smartdesklamp.smartdesklamp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;

@RestController
public class EfficiencyController {
    @Autowired
    TaskService taskService;

    User user=null;

    @ModelAttribute
    public void comment(HttpServletRequest request){
        user=(User)request.getAttribute("user");
    }
    /**
     * 添加任务
     * @param task
     * @return
     */
    @RequestMapping("/addTask")
    public RetJson addTask(Task task){
        Integer uid=user.getId();
        task.setUid(uid);
        if (!taskService.insertTask(task)){
            return RetJson.fail(-1,"添加失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 修改任务
     * @param task 任务
     * @return
     */
    @RequestMapping("/alterTask")
    public RetJson alterTask(Task task){
        if (!taskService.alterTask(task)){
            return RetJson.fail(-1,"修改失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 删除任务
     * @param id 任务id
     * @return
     */
    @RequestMapping("/deleteTask")
    public RetJson deleteTask(Integer id){
        Integer uid=user.getId();
        if (!taskService.deleteTaskById(uid,id)){
            return RetJson.fail(-1,"删除失败!");
        }
        return RetJson.succcess(null);
    }

    /**
     * 获取用户所有的任务
     * @return
     */
    @RequestMapping("/getTasks")
    public RetJson getTasks(){
        Integer uid=user.getId();
        Task[] tasks=taskService.queryTasksByUid(uid);
        return RetJson.succcess("tasks",tasks);
    }

    /**
     * 通过id获取任务
     * @param id
     * @return
     */
    @RequestMapping("/getTaskById")
    public RetJson getTaskById(Integer id){
        Integer uid=user.getId();
        Task task=taskService.queryTask(uid,id);
        if (task==null){
            return RetJson.fail(-1,"获取失败!");
        }
        return RetJson.succcess("task",task);
    }

    /**
     * 完成任务
     * @param id 任务id
     * @return
     */
    @RequestMapping("/completeTask")
    public RetJson completeTask(Integer id){
        Integer uid=user.getId();
        if (!taskService.completeTask(uid,id)){
            return RetJson.fail(-1,"状态修改失败!");
        }
        return RetJson.succcess(null);
    }
}
