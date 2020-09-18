package net.n2oapp.framework.examples.bpm;

import net.n2oapp.criteria.api.Criteria;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
public class BpmIntegration {

    @Autowired
    private ProcessEngine processEngine;

//    public List<ProcessDefinition> getProcesses() {
//        List<ProcessDefinition> result = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
//        return result;
//    }

    @SuppressWarnings("unused")
    public Integer getTasksCount() {
//        processEngine.getProcessEngineConfiguration().getDataSource()
        return processEngine.getTaskService().createTaskQuery().list().size();
    }

    @SuppressWarnings("unused")
    public List<Map<String, Object>> getTasks(Criteria criteria) {
        List<Task> list = processEngine.getTaskService().createTaskQuery().listPage(criteria.getFirst(), criteria.getSize());

        Map<String, ProcessDefinition> processDef = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Task task : list) {
            ProcessDefinition pd = processDef.get(task.getProcessDefinitionId());
            if (pd == null) {
                pd = processEngine.getRepositoryService().getProcessDefinition(task.getProcessDefinitionId());
                processDef.put(task.getProcessDefinitionId(), pd);
            }

            Map<String, Object> tsk = processEngine.getTaskService().getVariables(task.getId());
            tsk.putAll(map(task));
            tsk.put("processName", pd.getName());
            result.add(tsk);
        }

        return result;
    }

    @SuppressWarnings("unused")
    public Map<String, Object> getTask(String id) {
        Task task = processEngine.getTaskService().createTaskQuery().taskId(id).singleResult();
        Map<String, Object> result = processEngine.getTaskService().getVariables(id);
        result.putAll(map(task));
        return result;
    }

    private Map<String, Object> map(Task task) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", task.getId());
        result.put("name", task.getName());
        result.put("processDefinitionId", task.getProcessDefinitionId());
        result.put("taskDefinitionKey", task.getTaskDefinitionKey());
        return result;
    }
}
