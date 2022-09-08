package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
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
}
