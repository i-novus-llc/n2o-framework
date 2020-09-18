package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

import java.util.List;

/**
 * Клиентская модель валидации с диалогом выбора
 */
@Getter
@Setter
public class ValidationDialog extends Validation {
    private N2oInvocation invocation;
    private List<InvocationParameter> inParametersList;
    private List<InvocationParameter> outParametersList;
    private Toolbar toolbar;

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        dataSet = DomainProcessor.getInstance().doDomainConversation(dataSet, getInParametersList());
        DataSet result = serviceProvider.invoke(getInvocation(), dataSet, getInParametersList(), getOutParametersList());
        if (result.get(CompiledObject.VALIDATION_RESULT_PARAM) == null || !(boolean) result.get(CompiledObject.VALIDATION_RESULT_PARAM))
            callback.onFail(StringUtils.resolveLinks(getMessage(), result));
    }

    @Override
    public String getType() {
        return "dialog";
    }
}
