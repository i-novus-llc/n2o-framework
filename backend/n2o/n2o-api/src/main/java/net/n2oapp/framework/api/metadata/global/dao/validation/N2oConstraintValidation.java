package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperTypeEnum;

/**
 * Исходная модель валидации ограничений полей
 */
@Getter
@Setter
public class N2oConstraintValidation extends N2oInvocationValidation {
    private MapperTypeEnum mapper;
}
