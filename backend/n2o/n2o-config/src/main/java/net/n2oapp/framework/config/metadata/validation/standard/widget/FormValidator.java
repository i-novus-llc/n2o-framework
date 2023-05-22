package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Валидатор виджета форма
 */
@Component
public class FormValidator implements SourceValidator<N2oForm>, SourceClassAware {

    @Override
    public void validate(N2oForm source, SourceProcessor p) {
        FieldsScope fieldsScope = new FieldsScope();
        MetaActions actions = new MetaActions(
                p.safeStreamOf(source.getActions()).collect(Collectors.toMap(ActionBar::getId, Function.identity()))
        );
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        p.safeStreamOf(source.getItems()).forEach(item -> p.validate(item, fieldsScope, widgetScope, actions));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}