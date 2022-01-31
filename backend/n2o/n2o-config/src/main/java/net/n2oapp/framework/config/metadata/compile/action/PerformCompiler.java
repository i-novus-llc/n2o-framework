package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oPerform;
import net.n2oapp.framework.api.metadata.meta.action.Perform;
import org.springframework.stereotype.Component;

/**
 * Компиляция действия Perform
 */
@Component
public class PerformCompiler extends AbstractActionCompiler<Perform, N2oPerform> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPerform.class;
    }

    @Override
    public Perform compile(N2oPerform source, CompileContext<?,?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        Perform action = new Perform(p.mapAttributes(source));
        action.setType(source.getType());
        compileAction(action, source, p);

        return action;
    }
}

