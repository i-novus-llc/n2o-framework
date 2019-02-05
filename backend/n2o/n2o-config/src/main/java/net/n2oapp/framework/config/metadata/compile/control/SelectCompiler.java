package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.api.metadata.meta.control.Select;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;


@Component
public class SelectCompiler extends ListControlCompiler<Select, N2oSelect> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.select.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSelect.class;
    }

    @Override
    public StandardField<Select> compile(N2oSelect source, CompileContext<?,?> context, CompileProcessor p) {
        Select control = new Select();
        control.setHasCheckboxes(ListType.checkboxes == source.getType());
        control.setClosePopupOnSelect(!control.getHasCheckboxes());
        return compileListControl(control, source, context, p);
    }
}
