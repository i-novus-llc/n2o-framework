package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.multiform.MultiForm;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание мульти-формы с данными
 */
@Component
public class MultiFormBinder implements BaseMetadataBinder<MultiForm> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return MultiForm.class;
    }

    @Override
    public MultiForm bind(MultiForm multiForm, BindProcessor p) {
        if (multiForm.getComponent().getFieldsets() != null)
            multiForm.getComponent().getFieldsets().forEach(p::bind);
        return multiForm;
    }
}
