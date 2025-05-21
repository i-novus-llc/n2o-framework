package net.n2oapp.framework.boot.camunda;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с Camunda через rest
 */
public class RestCamundaProxyEngine implements CamundaProxyEngine {
    private static final String EXCEPTION_MESSAGE = "Not implemented yet...";

    @Value("${n2o.engine.camunda.rest_url}")
    private String restUrl;

    @Override
    public Long getCountTasks(Map<String, Object> inParams) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public List<ExtTask> findTasks(Map<String, Object> inParams) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public ExtTask getTask(String id) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }

    @Override
    public String startProcess(String processKey, Map<String, Object> variables) {
        throw new UnsupportedOperationException(EXCEPTION_MESSAGE);
    }
}
