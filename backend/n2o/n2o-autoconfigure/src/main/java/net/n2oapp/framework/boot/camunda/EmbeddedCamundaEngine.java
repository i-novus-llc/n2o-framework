package net.n2oapp.framework.boot.camunda;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmbeddedCamundaEngine implements CamundaEngine {

    private final ProcessEngine processEngine;

    public EmbeddedCamundaEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Override
    public Integer getCountTasks() {
        return processEngine.getTaskService().createTaskQuery().list().size();
    }

    @Override
    public List<ExtTask> findTasks(Map<String, Object> inParams) {
        List<Task> list = processEngine.getTaskService().createTaskQuery().list();

        Collection<String> variableNames = inParams.containsKey("select") ? (Collection<String>) inParams.get("select") : null;
        return list.stream().map(t -> map(t, variableNames == null || variableNames.isEmpty() ? null :
                processEngine.getTaskService().getVariables(t.getId(), variableNames))).collect(Collectors.toList());
    }

    @Override
    public ExtTask getTask(String id) {
        Task task = processEngine.getTaskService().createTaskQuery().taskId(id).singleResult();
        return map(task, processEngine.getTaskService().getVariables(task.getId()));
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        processEngine.getTaskService().setVariables(taskId, variables);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        processEngine.getTaskService().complete(taskId, variables);
    }

    @Override
    public String startProcess(String processKey, Map<String, Object> variables) {
        ProcessInstance process = processEngine.getRuntimeService().startProcessInstanceByKey(processKey, variables);
        return process.getId();
    }

    private ExtTaskImpl map(Task task, Map<String, Object> variables) {
        ExtTaskImpl result = new ExtTaskImpl();

        result.setId(task.getId());
        result.setName(task.getName());
        result.setPriority(task.getPriority());
        result.setOwner(task.getOwner());
        result.setAssignee(task.getAssignee());
        result.setProcessInstanceId(task.getProcessInstanceId());
        result.setExecutionId(task.getExecutionId());
        result.setProcessDefinitionId(task.getProcessDefinitionId());
        result.setCreateTime(task.getCreateTime());
        result.setTaskDefinitionKey(task.getTaskDefinitionKey());
        result.setDueDate(task.getDueDate());
        result.setFollowUpDate(task.getFollowUpDate());

        result.setVariables(variables);

        return result;
    }

}
