package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;

import java.util.List;

/**
 * Исходная модель валидаций ограничений.
 */
@Getter
@Setter
public class N2oConstraint extends N2oValidation {

    private N2oInvocation n2oInvocation;
    private N2oObject.Parameter[] inParameters;
    private N2oObject.Parameter[] outParameters;
    private String result;
    private MapperType mapper;

}
