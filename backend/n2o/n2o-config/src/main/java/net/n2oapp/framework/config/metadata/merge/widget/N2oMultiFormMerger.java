package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oMultiForm;
import org.springframework.stereotype.Component;

/**
 * Слияние виджетов {@code <multi-form>}
 */

@Component
public class N2oMultiFormMerger extends N2oWidgetMerger<N2oMultiForm> {

    @Override
    public N2oMultiForm merge(N2oMultiForm ref, N2oMultiForm source) {
        setIfNotNull(source::setPagination, source::getPagination, ref::getPagination);
        setIfNotNull(source::setUnsavedDataPrompt, source::getUnsavedDataPrompt, ref::getUnsavedDataPrompt);
        if (ref.getForm() == null)
            return source;

        if (source.getForm() == null)
            source.setForm(ref.getForm());
        else {
            addIfNotNull(ref.getForm(), source.getForm(), N2oForm::setItems, N2oForm::getItems);
        }
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiForm.class;
    }
}