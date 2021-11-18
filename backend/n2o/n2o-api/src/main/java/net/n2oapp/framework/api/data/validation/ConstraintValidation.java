package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Клиентская модель валидации ограничений полей
 */
@Getter
@Setter
public class ConstraintValidation extends InvocationValidation {
    private Set<String> requiredFields;

    public ConstraintValidation() {
    }

    public ConstraintValidation(ConstraintValidation validation) {
        super(validation);
        this.requiredFields = validation.getRequiredFields();
    }

    public void setInParametersList(List<AbstractParameter> inParametersList) {
        super.setInParametersList(inParametersList);
        this.requiredFields = inParametersList.stream()
                .filter(p -> Boolean.TRUE.equals(p.getRequired()))
                .map(AbstractParameter::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        dataSet = DomainProcessor.getInstance().doDomainConversation(dataSet, getInParametersList());
        DataSet result = serviceProvider.invoke(getInvocation(), dataSet, getInParametersList(), getOutParametersList());
        if (result.get(CompiledObject.VALIDATION_RESULT_PARAM) == null || !(boolean) result.get(CompiledObject.VALIDATION_RESULT_PARAM))
            callback.onFail(StringUtils.resolveLinks(String.valueOf(getMessage()), result));
    }

    @Override
    public String getType() {
        return "constraint";
    }
}
