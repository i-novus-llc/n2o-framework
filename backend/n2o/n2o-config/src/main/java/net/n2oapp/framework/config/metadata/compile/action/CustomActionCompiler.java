package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.meta.action.CustomAction;
import org.springframework.stereotype.Component;

/**
 * Компиляция кастомизированного действия
 */
@Component
public class CustomActionCompiler extends AbstractActionCompiler<CustomAction, N2oCustomAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomAction.class;
    }

    @Override
    public CustomAction compile(N2oCustomAction source, CompileContext<?,?> context, CompileProcessor p) {
        CustomAction action = new CustomAction(source.getProperties());
        compileAction(action, source, p);
        return action;
    }
}
