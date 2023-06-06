package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.control.N2oCustomField;
import net.n2oapp.framework.api.metadata.meta.control.CustomField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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
        initDefaults(source, context, p);
        compileField(customField, source, context, p);
        if (source.getControls() != null) {
            if (source.getControls().length > 1) {
                customField.setControls(new ArrayList<>());
                for (N2oComponent component : source.getControls()) {
                    customField.getControls().add(compileControl(component, context, p));
                }
            } else if (source.getControls().length == 1) {
                customField.setControl(compileControl(source.getControls()[0], context, p));
            }
        }
        return customField;
    }

    protected net.n2oapp.framework.api.metadata.Component compileControl(N2oComponent source,
                                                                         CompileContext<?, ?> context, CompileProcessor p) {
        net.n2oapp.framework.api.metadata.Component control = p.compile(source, context);
        if (control instanceof StandardField) {
            return ((StandardField) control).getControl();
        }
        return control;
    }
}
