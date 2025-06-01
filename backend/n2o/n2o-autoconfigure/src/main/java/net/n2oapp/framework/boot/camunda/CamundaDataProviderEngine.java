package net.n2oapp.framework.boot.camunda;

import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oCamundaDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Сервис для выполнения запросов к Camunda
 */
public class CamundaDataProviderEngine implements MapInvocationEngine<N2oCamundaDataProvider> {

    @Autowired
    private CamundaProxyEngine engine;

    @Override
    public Object invoke(N2oCamundaDataProvider invocation, Map<String, Object> inParams) {
        if (invocation.getOperation() == null)
            return engine.findTasks(inParams);

        switch (invocation.getOperation()) {
            case COUNT_TASKS:
                return engine.getCountTasks(inParams);
            case FIND_TASKS:
                return engine.findTasks(inParams);
            case GET_TASK:
                return getTask(inParams);
            case SET_TASK_VARIABLES:
                return setTaskVariables(inParams);
            case COMPLETE_TASK:
                return completeTask(inParams);
            case START_PROCESS:
                return startProcess(inParams);
        }

        return null;
    }

    @Override
    public Class<? extends N2oCamundaDataProvider> getType() {
        return N2oCamundaDataProvider.class;
    }

    private String extractId(Map<String, Object> inParams) {
        Object id = inParams.remove("id");
        return id == null ? null : id.toString();
    }

    private Object getTask(Map<String, Object> inParams) {
        String id = extractId(inParams);
        return id == null ? null : engine.getTask(id);
    }

    private Boolean setTaskVariables(Map<String, Object> inParams) {
        String id = extractId(inParams);
        if (id != null) engine.setTaskVariables(id, inParams);
        return true;
    }

    private Boolean completeTask(Map<String, Object> inParams) {
        String id = extractId(inParams);
        if (id != null) engine.completeTask(id, inParams);
        return true;
    }

    private String startProcess(Map<String, Object> inParams) {
        String id = extractId(inParams);
        return id == null ? null : engine.startProcess(id, inParams);
    }
}
