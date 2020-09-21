package net.n2oapp.framework.boot.camunda;

import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oCamundaDataProvider;

import java.util.Map;

public class CamundaDataProviderEngine implements MapInvocationEngine<N2oCamundaDataProvider> {

    private final CamundaProxyEngine engine;

    public CamundaDataProviderEngine(CamundaProxyEngine engine) {
        this.engine = engine;
    }

    @Override
    public Object invoke(N2oCamundaDataProvider invocation, Map<String, Object> inParams) {
        if (invocation.getOperation() == null)
            return engine.findTasks(inParams);

        switch (invocation.getOperation()) {
            case countTasks:
                return engine.getCountTasks(inParams);
            case findTasks:
                return engine.findTasks(inParams);
            case getTask:
                return getTask(inParams);
            case setTaskVariables:
                return setTaskVariables(inParams);
            case completeTask:
                return completeTask(inParams);
            case startProcess:
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
