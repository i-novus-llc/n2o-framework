package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Клиентская модель Constraint валидации
 */
@Getter
@Setter
@NoArgsConstructor
public class ConstraintValidation extends Validation {
    private Set<String> requiredFields;
    private N2oInvocation invocation;
    private List<InvocationParameter> inParameterList;
    private List<InvocationParameter> outParametersList;

    public ConstraintValidation(ConstraintValidation validation) {
        super(validation);
        this.requiredFields = validation.getRequiredFields();
        this.invocation = validation.getInvocation();
        this.inParameterList = validation.getInParameterList();
        this.outParametersList = validation.getOutParametersList();
    }

    public void setInParameterList(List<InvocationParameter> inParameterList) {
        this.requiredFields = inParameterList.stream()
                .filter(p -> p.getRequired() != null && p.getRequired())
                .map(InvocationParameter::getId)
                .collect(Collectors.toSet());
        this.inParameterList = inParameterList;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        dataSet = DomainProcessor.getInstance().doDomainConversation(dataSet, getInParameterList());
        DataSet result = serviceProvider.invoke(getInvocation(), dataSet, getInParameterList(), getOutParametersList());
        if (result.get(CompiledObject.VALIDATION_RESULT_PARAM) == null || !(boolean) result.get(CompiledObject.VALIDATION_RESULT_PARAM)) {
            callback.onFail(StringUtils.resolveLinks(getMessage(), result));
        }
    }

    @Override
    public String getType() {
        return "constraint";
    }

}
