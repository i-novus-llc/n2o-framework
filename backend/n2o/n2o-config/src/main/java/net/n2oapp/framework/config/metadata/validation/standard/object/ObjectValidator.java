package net.n2oapp.framework.config.metadata.validation.standard.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.ValidationUtil.checkIdsUnique;

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
    public void validate(N2oObject n2oObject) throws N2oMetadataValidationException {
        checkIdsUnique(n2oObject.getObjectFields(), "Поле '%s' встречается более чем один раз в объекте '" + n2oObject.getId() + "'!");
        checkIdsUnique(n2oObject.getOperations(), "Action '%s' встречается более чем один раз в объекте '" + n2oObject.getId() + "'!");
        checkIdsUnique(n2oObject.getN2oValidations(), "Валидация '%s' встречается более чем один раз в объекте '" + n2oObject.getId() + "'!");
    }

}
