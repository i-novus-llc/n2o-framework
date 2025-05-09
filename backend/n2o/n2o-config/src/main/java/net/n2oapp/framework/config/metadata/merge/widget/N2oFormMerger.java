package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов Форма
 */
@Component
public class N2oFormMerger extends N2oWidgetMerger<N2oForm> {
    @Override
    public N2oForm merge(N2oForm ref, N2oForm source) {
        setIfNotNull(source::setMode, source::getMode, ref::getMode);
        setIfNotNull(source::setUnsavedDataPrompt, source::getUnsavedDataPrompt, ref::getUnsavedDataPrompt);
        setIfNotNull(source::setDefaultValuesQueryId, source::getDefaultValuesQueryId, ref::getDefaultValuesQueryId);
        addIfNotNull(ref, source, N2oForm::setItems, N2oForm::getItems);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oForm.class;
    }
}
