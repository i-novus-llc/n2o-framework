package net.n2oapp.framework.config.metadata.validation.standard.widget.columns;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oMultiColumn;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MultiColumnValidator implements SourceValidator<N2oMultiColumn>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiColumn.class;
    }

    @Override
    public void validate(N2oMultiColumn source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getChildren() != null)
            Arrays.stream(source.getChildren()).forEach(children -> p.validate(children, widgetScope));
    }
}
