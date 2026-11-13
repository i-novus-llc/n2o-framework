package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Валидатор кастомного действия
 */
@Component
public class CustomActionValidator extends AbstractMetaActionValidator<N2oCustomAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomAction.class;
    }

    @Override
    public void validate(N2oCustomAction source, SourceProcessor p) {
        super.validate(source, p);
        checkType(source);
    }

    private static void checkType(N2oCustomAction source) {
        String type = source.getType();
        if (type == null || type.isBlank())
            throw new N2oMetadataValidationException(
                    "В кастомном действии \"<action>\" не указан обязательный атрибут 'type'"
            );
    }
}
