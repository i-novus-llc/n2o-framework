package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
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
    private RedirectSaga redirect;
    private RefreshSaga refresh;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;

    //mutable
    private Map<String, AbstractParameter> inParametersMap = new LinkedHashMap<>();
    private Map<String, ObjectSimpleField> outParametersMap = new LinkedHashMap<>();
    /**
     * "Сырые" данные, не приведенные к домену
     */
    private D data;

    public void setOperation(CompiledObject.Operation operation) {
        this.operation = operation;
        if (operation.getInParametersMap() != null)
            for (String paramId : operation.getInParametersMap().keySet()) {
                AbstractParameter sourceParam = operation.getInParametersMap().get(paramId);
                AbstractParameter param;
                if (sourceParam instanceof ObjectSimpleField)
                    param = new ObjectSimpleField((ObjectSimpleField) sourceParam);
                else if (sourceParam instanceof ObjectListField)
                    param = new ObjectListField((ObjectListField) sourceParam);
                else if (sourceParam instanceof ObjectSetField)
                    param = new ObjectSetField((ObjectSetField) sourceParam);
                else
                    param = new ObjectReferenceField((ObjectReferenceField) sourceParam);
                inParametersMap.put(paramId, param);
            }
        if (operation.getOutParametersMap() != null)
            for (String paramName : operation.getOutParametersMap().keySet()) {
                ObjectSimpleField srcParam = operation.getOutParametersMap().get(paramName);
                outParametersMap.put(paramName, new ObjectSimpleField(srcParam));
            }
    }
}
