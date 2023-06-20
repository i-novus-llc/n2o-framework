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
        if (source != null)
            for (String paramId : source.keySet()) {
                AbstractParameter sourceParam = source.get(paramId);
                AbstractParameter param;
                if (sourceParam instanceof ObjectSimpleField)
                    param = new ObjectSimpleField((ObjectSimpleField) sourceParam);
                else if (sourceParam instanceof ObjectListField)
                    param = new ObjectListField((ObjectListField) sourceParam);
                else if (sourceParam instanceof ObjectSetField)
                    param = new ObjectSetField((ObjectSetField) sourceParam);
                else
                    param = new ObjectReferenceField((ObjectReferenceField) sourceParam);
                result.put(paramId, param);
            }
    }
}
