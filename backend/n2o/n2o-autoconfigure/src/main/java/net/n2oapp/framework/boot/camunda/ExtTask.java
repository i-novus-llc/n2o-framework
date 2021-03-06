package net.n2oapp.framework.boot.camunda;

import java.util.Date;
import java.util.Map;

/**
 * Представление задачи пользователя (org.camunda.bpm.engine.task.Task) с переменными
 */
public interface ExtTask {

    String getId();

    String getName();

    Integer getPriority();

    String getOwner();

    String getAssignee();

    String getProcessInstanceId();

    String getExecutionId();

    String getProcessDefinitionId();

    Date getCreateTime();

    String getTaskDefinitionKey();

    Date getDueDate();

    Date getFollowUpDate();

    Map<String, Object> getVariables();
}
