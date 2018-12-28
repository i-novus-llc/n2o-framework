package net.n2oapp.framework.api.metadata.global.dao.validation;


import lombok.Getter;
import lombok.Setter;


/**
 * Исходная модель валидаций обязательности.
 */
@Getter
@Setter
public class N2oMandatory extends N2oValidation {
    private String expression;
    private String expressionOn;
    private String src;
}
