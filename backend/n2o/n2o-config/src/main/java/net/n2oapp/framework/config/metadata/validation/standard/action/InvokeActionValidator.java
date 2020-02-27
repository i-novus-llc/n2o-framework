package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор InvokeAction
 */
@Component
public class InvokeActionValidator implements SourceValidator<N2oInvokeAction>, SourceClassAware {
    @Override
    public void validate(N2oInvokeAction source, ValidateProcessor p) {
        if (source.getRefreshWidgetId() != null) {
            p.checkForExists(source.getRefreshWidgetId(), N2oWidget.class, "Атрибут refresh-widget-id ссылается на несуществующий виджет: " + source.getRefreshWidgetId());
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }
}
