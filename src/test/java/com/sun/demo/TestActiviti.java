package com.sun.demo;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestActiviti {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private HistoryService historyService;

    /**
     * spring boot自动将流程定义
     * TODO启动流程实例
     **/
    @Test
    public void startProcess() {
        ProcessInstance p = runtimeService.startProcessInstanceByKey("myLeave");
        System.out.println("流程定义的id,对应act_re_procdef 的id_" + p.getProcessDefinitionId());
        System.out.println("流程实例的id" + p.getId());
        System.out.println(p.getActivityId());
    }


    /**
     * 查询当前部署的myLeave有几个流程实例在跑
     */
    @Test
    public void processInstance() {
        ProcessInstanceQuery myLeave = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("myLeave");
        List<ProcessInstance> list = myLeave.list();
        for (ProcessInstance instance : list) {
            System.out.println("流程实例ID：" + instance.getProcessInstanceId());
            System.out.println("所属流程定义ID：" + instance.getProcessDefinitionId());
            System.out.println("是否执行完成：" + instance.isEnded());
            System.out.println("是否暂停：" + instance.isSuspended());
            System.out.println("当前活动标识：" + instance.getActivityId());
            System.out.println("业务关键字：" + instance.getBusinessKey());
            System.out.println("--------------------");
        }
    }
    
    
    @Test
    public void findTaskListByAssignee() {
        // 任务负责人
        String assignee = "manager";
        // 根据流程id，任务负责人查询待办任务
        TaskQuery myLeave = taskService.createTaskQuery()
                .processDefinitionKey("myLeave")
                .taskAssignee(assignee);
        List<Task> list = myLeave.list();
        for (Task task : list) {
            System.out.println("流程实例ID：目前看来是启动流程实例时在act_ru_execution表生成的第一条id" + task.getProcessInstanceId());
            System.out.println("任务ID" + task.getId());
            System.out.println("任务负责人" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
            System.out.println("--------------------");
        }
    }
    
    
    /**
     * 处理任务
     **/
    @Test
    public void completeTask() {
        // 任务ID
        String taskId = "2ea11b74-ba12-11eb-84a2-62f262d6f7c2";
        // 完成任务
        taskService.complete(taskId);
    }


    /**
     * 删除流程实例
     */
    @Test
    public void deleteDeployment() {
        repositoryService.deleteDeployment("d630c5d7-ba07-11eb-8dc1-62f262d6f7c2");
    }



}
