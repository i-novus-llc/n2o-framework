package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;

/**
 * Абстрактная модель валидации с вызовом провайдера данных операции
 */
@Getter
@Setter
public abstract class N2oInvocationValidation extends N2oValidation {
    private N2oInvocation n2oInvocation;
    private AbstractParameter[] inFields;
    private ObjectSimpleField[] outFields;
    // TODO будут убраны в следующей задаче
    private N2oObject.Parameter[] inParameters;
    private N2oObject.Parameter[] outParameters;
}
