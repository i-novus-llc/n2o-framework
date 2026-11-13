package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oValidateAction;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Валидатор действия валидации
 */
@Component
public class ValidateActionValidator implements SourceValidator<N2oValidateAction>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oValidateAction.class;
    }

    @Override
    public void validate(N2oValidateAction source, SourceProcessor p) {
        if (source.getFields() == null)
            return;

        for (N2oValidateAction.Field field : source.getFields()) {
            if (field.getId() == null || field.getId().isBlank())
                throw new N2oMetadataValidationException(
                        "В действии \"<validate>\" в элементе \"<field>\" не задан обязательный атрибут 'id'");
        }
    }
}