package net.n2oapp.framework.boot.camunda;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public class RestCamundaProxyEngine implements CamundaProxyEngine  {

    @Value("${n2o.engine.camunda.rest_url}")
    private String restUrl;

    @Override
    public Long getCountTasks(Map<String, Object> inParams) {
        return null;
    }

    @Override
    public List<ExtTask> findTasks(Map<String, Object> inParams) {
        return null;
    }

    @Override
    public ExtTask getTask(String id) {
        return null;
    }

    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {

    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {

    }

    @Override
    public String startProcess(String processKey, Map<String, Object> variables) {
        return null;
    }
}
