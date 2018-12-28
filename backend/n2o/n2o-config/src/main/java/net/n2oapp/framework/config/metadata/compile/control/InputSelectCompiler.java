package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class InputSelectCompiler extends ListControlCompiler<InputSelect, N2oInputSelect> {

    @Override
    public StandardField<InputSelect> compile(N2oInputSelect source, CompileContext<?,?> context, CompileProcessor p) {
        InputSelect inputSelect = new InputSelect();
        inputSelect.setResetOnBlur(!p.cast(source.getStoreOnInput(), false));
        inputSelect.setPlaceholder(p.resolveJS(source.getPlaceholder()));
        inputSelect.setControlSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.control.input.select.src"), String.class)));
        ListType type = p.cast(source.getType(), ListType.single);
        switch (type) {
            case checkboxes:
                inputSelect.setHasCheckboxes(true);
                inputSelect.setMultiSelect(true);
                break;
            case multi:
                inputSelect.setHasCheckboxes(false);
                inputSelect.setMultiSelect(true);
                break;
            case single:
                inputSelect.setHasCheckboxes(false);
                inputSelect.setMultiSelect(false);
                break;
        }
        return compileListControl(inputSelect, source, context, p);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInputSelect.class;
    }
}
