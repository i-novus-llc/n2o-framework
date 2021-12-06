package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

/**
 * Связывание формы с данными
 */
@Component
public class FormBinder implements BaseMetadataBinder<Form> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Form.class;
    }

    @Override
    public Form bind(Form form, BindProcessor p) {
       /* if (form.getFormDataProvider() != null) {
            BindUtil.bindDataProvider(form.getFormDataProvider(), p);
        }*/
        if (form.getComponent().getFieldsets() != null)
            form.getComponent().getFieldsets().forEach(p::bind);
        return form;
    }
}
