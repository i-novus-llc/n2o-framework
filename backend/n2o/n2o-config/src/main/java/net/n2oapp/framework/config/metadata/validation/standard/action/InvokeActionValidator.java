package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import org.springframework.stereotype.Component;

/**
 * Валидатор InvokeAction
 */
@Component
public class InvokeActionValidator extends AbstractMetaActionValidator<N2oInvokeAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }
}
