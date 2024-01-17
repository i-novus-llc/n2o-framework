package net.n2oapp.framework.config.metadata.compile.control.filters_buttons;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oClearButton;
import net.n2oapp.framework.api.metadata.meta.control.filters_buttons.ClearButton;
import org.springframework.stereotype.Component;

@Component
public class ClearButtonCompiler extends AbstractFilterButtonCompiler<ClearButton, N2oClearButton> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClearButton.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.clear_button.src";
    }

    @Override
    protected String getDefaultLabel(CompileProcessor p) {
        return p.getMessage("n2o.api.control.clear_button.label");
    }

    @Override
    public ClearButton compile(N2oClearButton source, CompileContext<?, ?> context, CompileProcessor p) {
        ClearButton button = new ClearButton();
        compileButton(button, source, context, p);
        return button;
    }
}
