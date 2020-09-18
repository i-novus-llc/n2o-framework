package net.n2oapp.framework.boot.camunda;

import java.util.List;
import java.util.Map;

public interface CamundaEngine {

    Integer getCountTasks();

    List<ExtTask> findTasks(Map<String, Object> inParams);

    ExtTask getTask(String id);

    void setTaskVariables(String taskId, Map<String, Object> variables);

    void completeTask(String taskId, Map<String, Object> variables);

    String startProcess(String processKey, Map<String, Object> variables);
}
