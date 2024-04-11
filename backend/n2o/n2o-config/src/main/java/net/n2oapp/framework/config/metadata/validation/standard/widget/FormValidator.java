package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

/**
 * Валидатор виджета форма
 */
@Component
public class FormValidator extends WidgetValidator<N2oForm> {

    @Override
    public void validate(N2oForm source, SourceProcessor p) {
        super.validate(source, p);
        FieldsScope fieldsScope = new FieldsScope();
        MetaActions actions = getAllMetaActions(p.getScope(MetaActions.class), source.getActions(), p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        p.safeStreamOf(source.getItems()).forEach(item -> p.validate(item, fieldsScope, widgetScope));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}