package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор объекта
 */
@Component
public class ObjectValidator implements SourceValidator<N2oObject>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oObject.class;
    }

    @Override
    public void validate(N2oObject n2oObject, ValidateProcessor p) {
        p.checkIdsUnique(n2oObject.getObjectFields(), "Поле {0} встречается более чем один раз в объекте " + n2oObject.getId());
        p.checkIdsUnique(n2oObject.getOperations(), "Действие {0} встречается более чем один раз в объекте " + n2oObject.getId());
        p.checkIdsUnique(n2oObject.getN2oValidations(), "Валидация {0} встречается более чем один раз в объекте " + n2oObject.getId());
    }

}
