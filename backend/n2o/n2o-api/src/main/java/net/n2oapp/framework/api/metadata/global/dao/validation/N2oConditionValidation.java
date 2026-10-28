package net.n2oapp.framework.api.metadata.global.dao.validation;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oFileAware;

/**
 * Исходная модель валидации условия значений полей
 */
@Getter
@Setter
public class N2oConditionValidation extends N2oValidation implements N2oFileAware {
    private String expression;
    private String[] expressionOn;
    private String file;
}