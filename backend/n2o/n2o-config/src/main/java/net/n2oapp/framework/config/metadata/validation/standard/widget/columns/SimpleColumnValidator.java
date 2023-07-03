package net.n2oapp.framework.config.metadata.validation.standard.widget.columns;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

@Component
public class SimpleColumnValidator implements SourceValidator<N2oSimpleColumn>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleColumn.class;
    }

    @Override
    public void validate(N2oSimpleColumn source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);

        if (source.getCell() != null)
            p.validate(source.getCell(), widgetScope);
    }
}
