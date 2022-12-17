package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.CheckboxDefaultValueEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oCheckbox;
import net.n2oapp.framework.api.metadata.meta.control.Checkbox;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция чекбокса
 */

@Component
public class CheckboxCompiler extends StandardFieldCompiler<Checkbox, N2oCheckbox> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCheckbox.class;
    }

    @Override
    public StandardField<Checkbox> compile(N2oCheckbox source, CompileContext<?,?> context, CompileProcessor p) {
        Checkbox checkbox = new Checkbox();
        CheckboxDefaultValueEnum defaultUnchecked = p.resolve(property("n2o.api.control.checkbox.unchecked"), CheckboxDefaultValueEnum.class);
        if (CheckboxDefaultValueEnum.FALSE == source.getUnchecked() ||
                source.getUnchecked() == null && CheckboxDefaultValueEnum.FALSE == defaultUnchecked) {
            checkbox.setDefaultUnchecked(CheckboxDefaultValueEnum.FALSE.getId());
            if (source.getDefaultValue() == null)
                source.setDefaultValue("false");
        } else {
            checkbox.setDefaultUnchecked(CheckboxDefaultValueEnum.NULL.getId());
        }
        StandardField<Checkbox> field = compileStandardField(checkbox, source, context, p);
        if (field.getLabel() != null) {
            checkbox.setLabel(p.resolveJS(field.getLabel()));
        }
        field.setLabel(null);
        return field;
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.checkbox.src";
    }
}
