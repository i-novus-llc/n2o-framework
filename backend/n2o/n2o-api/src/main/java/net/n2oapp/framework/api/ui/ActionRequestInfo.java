package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.LoadingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.PollingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Информация о запросе вызова операции
 */
@Getter
@Setter
public class ActionRequestInfo<D> extends RequestInfo {

    //immutable
    private CompiledObject object;
    private CompiledObject.Operation operation;
    private String clearDatasource;
    private String pollingEndCondition;
    private PollingSaga polling;
    private LoadingSaga loading;
    private RedirectSaga redirect;
    private RefreshSaga refresh;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;
    private boolean useFailOut;

    //mutable
    private Map<String, AbstractParameter> inParametersMap = new LinkedHashMap<>();
    private Map<String, AbstractParameter> outParametersMap = new LinkedHashMap<>();
    /**
     * "Сырые" данные, не приведенные к домену
     */
    private D data;

    public void setOperation(CompiledObject.Operation operation) {
        this.operation = operation;
        copyParams(operation.getInParametersMap(), inParametersMap);
        copyParams(operation.getOutParametersMap(), outParametersMap);
    }

    private void copyParams(Map<String, AbstractParameter> source, Map<String, AbstractParameter> result) {
        if (source == null) return;
        for (Map.Entry<String, AbstractParameter> entry : source.entrySet()) {
            AbstractParameter sourceParam = entry.getValue();
            AbstractParameter param;
            if (sourceParam instanceof ObjectSimpleField o)
                param = new ObjectSimpleField(o);
            else if (sourceParam instanceof ObjectListField o)
                param = new ObjectListField(o);
            else if (sourceParam instanceof ObjectSetField o)
                param = new ObjectSetField(o);
            else
                param = new ObjectReferenceField((ObjectReferenceField) sourceParam);
            result.put(entry.getKey(), param);
        }
    }
}
