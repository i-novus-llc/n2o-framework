package net.n2oapp.framework.boot.camunda;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы со встроенным движком Camunda
 */
public class EmbeddedCamundaProxyEngine implements CamundaProxyEngine {

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public Long getCountTasks(Map<String, Object> inParams) {
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        Collection<String> filters = getValue(inParams, "filters", null);
        Collection<String> variableNames = getValue(inParams, "select", null);

        if (filters != null)
            filters.forEach(filter -> createFilter(taskQuery, filter, variableNames, inParams));

        return taskQuery.count();
    }

    @Override
    public List<ExtTask> findTasks(Map<String, Object> inParams) {
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        Collection<String> filters = getValue(inParams, "filters", null);
        Collection<String> variableNames = getValue(inParams, "select", null);
        if (filters != null)
            filters.forEach(filter -> createFilter(taskQuery, filter, variableNames, inParams));

        Integer limit = getValue(inParams, "limit", 10);
        Integer page = getValue(inParams, "page", 1);

        List<Task> list = taskQuery.listPage((page - 1) * limit, limit);

        return list.stream().map(t -> (ExtTask) map(t, variableNames == null || variableNames.isEmpty() ? null :
                processEngine.getTaskService().getVariables(t.getId(), variableNames))).toList();
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

    @SuppressWarnings("unchecked")
    private <T> T getValue(Map<String, Object> inParams, String name, T def) {
        Object val = inParams.get(name);
        return val == null ? def : (T) val;
    }

    private void createFilter(TaskQuery taskQuery, String filter, Collection<String> variableNames, Map<String, Object> inParams) {
        Object value = inParams.get(filter);
        if (value == null) return;

        switch (filter) {   //https://docs.camunda.org/manual/7.7/reference/rest/task/get-query/
            case "id":
            case "taskId":
                taskQuery.taskId((String) value);
                break;
            case "processInstanceId":
                taskQuery.processInstanceId((String) value);
                break;
            case "processInstanceBusinessKey":
                taskQuery.processInstanceBusinessKey((String) value);
                break;
            case "processDefinitionId":
                taskQuery.processDefinitionId((String) value);
                break;
            case "processDefinitionKey":
                taskQuery.processDefinitionKey((String) value);
                break;
            case "processDefinitionName":
                taskQuery.processDefinitionName((String) value);
                break;
            case "executionId":
                taskQuery.executionId((String) value);
                break;
            case "assignee":
                taskQuery.taskAssignee((String) value);
                break;
            case "owner":
                taskQuery.taskOwner((String) value);
                break;
            case "candidateGroup":
                taskQuery.taskCandidateGroup((String) value);
                break;
            case "candidateUser":
                taskQuery.taskCandidateUser((String) value);
                break;
            case "assigned":
                if (Boolean.TRUE.equals(value)) taskQuery.taskAssigned();
                else taskQuery.taskUnassigned();
                break;
            case "unassigned":
                if (Boolean.TRUE.equals(value)) taskQuery.taskUnassigned();
                else taskQuery.taskAssigned();
                break;
            case "taskDefinitionKey":
                taskQuery.taskDefinitionKey((String) value);
                break;
            case "name":
                taskQuery.taskName((String) value);
                break;
            case "priority":
                taskQuery.taskPriority((Integer) value);
                break;
            case "dueDate":
                taskQuery.dueDate((Date) value);
                break;
            case "createdOn":
                taskQuery.taskCreatedOn((Date) value);
                break;
            case "active":
                if (Boolean.TRUE.equals(value)) taskQuery.active();
                break;
            case "suspended":
                if (Boolean.TRUE.equals(value)) taskQuery.suspended();
                break;
            case "parentTaskId":
                taskQuery.taskParentTaskId((String) value);
                break;
            default:
                if (variableNames.contains(filter)) {
                    taskQuery.processVariableValueEquals(filter, value);
                }
        }
    }
}
