package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;

import java.util.List;

/**
 * Клиентская модель валидации с вызовом провайдера данных операции
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class InvocationValidation extends Validation {
    private N2oInvocation invocation;
    private List<AbstractParameter> inParametersList;
    private List<AbstractParameter> outParametersList;

    protected InvocationValidation(InvocationValidation validation) {
        super(validation);
        this.invocation = validation.getInvocation();
        this.inParametersList = validation.getInParametersList();
        this.outParametersList = validation.getOutParametersList();
    }
}
