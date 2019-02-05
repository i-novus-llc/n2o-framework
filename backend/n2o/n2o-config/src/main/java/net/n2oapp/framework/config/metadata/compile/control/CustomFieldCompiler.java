package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import org.springframework.stereotype.Component;

/**
 * Компиляция кастомного поля для ввода
 */
@Component
public class CustomFieldCompiler extends FieldCompiler<CustomField, N2oCustomField> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomField.class;
    }

    @Override
    public CustomField compile(N2oCustomField source, CompileContext<?,?> context, CompileProcessor p) {
        CustomField customField = new CustomField();
        compileField(customField, source, context, p);
        return customField;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.field.src";
    }
}
