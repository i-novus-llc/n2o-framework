package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор InvokeAction
 */
@Component
public class InvokeActionValidator implements SourceValidator<N2oInvokeAction>, SourceClassAware {
    @Override
    public void validate(N2oInvokeAction source, ValidateProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getRefreshWidgetId() != null && pageScope != null && pageScope.getPage() != null) {
            p.checkNotNull(p.safeStreamOf(pageScope.getPage().getContainers()).filter(w -> source.getRefreshWidgetId().equals(w.getId())).findAny().orElse(null),
                    "Атрибут refresh-widget-id ссылается на несуществующий виджет: " + source.getRefreshWidgetId());
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }
}
