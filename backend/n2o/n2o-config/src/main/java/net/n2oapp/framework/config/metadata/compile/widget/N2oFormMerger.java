package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов Форма
 */
@Component
public class N2oFormMerger extends N2oWidgetMerger<N2oForm> {
    @Override
    public N2oForm merge(N2oForm source, N2oForm override) {
        setIfNotNull(source::setMode, override::getMode);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}
