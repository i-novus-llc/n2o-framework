package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
    public CustomField compile(N2oCustomField source, CompileContext<?, ?> context, CompileProcessor p) {
        CustomField customField = new CustomField();
        compileField(customField, source, context, p);
        if (source.getControls() != null) {
            if (source.getControls().length > 1) {
                customField.setControls(new ArrayList<>());
                for (N2oComponent component : source.getControls()) {
                    customField.getControls().add(p.compile(component, context));
                }
            } else if (source.getControls().length == 1) {
                customField.setControl(p.compile(source.getControls()[0], context));
            }
        }
        return customField;
    }

}
