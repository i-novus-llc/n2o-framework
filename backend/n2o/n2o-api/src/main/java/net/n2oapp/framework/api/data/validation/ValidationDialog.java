package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetMapper;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Клиентская модель валидации с диалогом выбора
 */
@Getter
@Setter
public class ValidationDialog extends Validation {
    private N2oInvocation invocation;
    private N2oDialog dialog;
    private List<AbstractParameter> inParametersList;
    private List<ObjectSimpleField> outParametersList;

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        dataSet = DomainProcessor.getInstance().doDomainConversation(dataSet, getInParametersList());
        DataSet result;
        if (invocation != null)
            result = serviceProvider.invoke(getInvocation(), dataSet, getInParametersList(), getOutParametersList());
        else {
            Map<String, String> outMapping = new LinkedHashMap<>();
            if (outParametersList != null)
                for (ObjectSimpleField parameter : outParametersList)
                    outMapping.put(parameter.getId(), parameter.getMapping());
            result = DataSetMapper.extract(dataSet, outMapping);
        }

        if (result.get(CompiledObject.VALIDATION_RESULT_PARAM) == null || !(boolean) result.get(CompiledObject.VALIDATION_RESULT_PARAM))
            callback.onFail(StringUtils.resolveLinks(getMessage(), result));
    }

    @Override
    public String getType() {
        return "dialog";
    }
}
