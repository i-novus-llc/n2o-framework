package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import org.springframework.stereotype.Component;

@Component
public class ListWidgetValidator extends AbstractListWidgetValidator<N2oListWidget> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListWidget.class;
    }

    @Override
    public void validate(N2oListWidget source, SourceProcessor p) {
        super.validate(source, p);
    }
}
