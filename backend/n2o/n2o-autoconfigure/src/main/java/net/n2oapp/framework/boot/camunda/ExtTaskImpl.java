package net.n2oapp.framework.boot.camunda;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * Представление задачи пользователя (org.camunda.bpm.engine.task.Task) с переменными
 */
@Getter
@Setter
public class ExtTaskImpl implements ExtTask {

    private String id;

    private String name;

    private Integer priority;

    private String owner;

    private String assignee;

    private String processInstanceId;

    private String executionId;

    private String processDefinitionId;

    private Date createTime;

    private String taskDefinitionKey;

    private Date dueDate;

    private Date followUpDate;

    private Map<String, Object> variables;

}
