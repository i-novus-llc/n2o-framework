package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oCustomControl;
import net.n2oapp.framework.api.metadata.meta.control.CustomControl;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import org.springframework.stereotype.Component;

/**
 * Сборка компонентов ввода
 */
@Component
public class CustomControlCompiler extends ComponentCompiler<CustomControl, N2oCustomControl, CompileContext<?, ?>> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomControl.class;
    }

    @Override
    public CustomControl compile(N2oCustomControl source, CompileContext<?, ?> context, CompileProcessor p) {
        CustomControl control = new CustomControl();
        compileComponent(control, source, context, p);
        return control;
    }
}
