package net.n2oapp.framework.config.metadata.validation.standard.widget.columns;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oBlock;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

@Component
public class BlockValidator implements SourceValidator<N2oBlock>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBlock.class;
    }

    @Override
    public void validate(N2oBlock source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getComponent() != null)
            p.validate(source.getComponent(), widgetScope);
    }
}
