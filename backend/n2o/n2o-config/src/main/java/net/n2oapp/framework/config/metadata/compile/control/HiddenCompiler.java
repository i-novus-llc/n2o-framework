package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oHidden;
import net.n2oapp.framework.api.metadata.meta.control.Hidden;
import org.springframework.stereotype.Component;

/**
 * Компиляция компонента hidden (Скрытый компонент ввода)
 */
@Component
public class HiddenCompiler extends FieldCompiler<Hidden, N2oHidden> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.control.hidden.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHidden.class;
    }

    @Override
    public Hidden compile(N2oHidden source, CompileContext<?, ?> context, CompileProcessor p) {
        Hidden hidden = new Hidden();
        initDefaults(source, context, p);
        compileField(hidden, source, context, p);
        compileFilters(source, p);
        compileDefaultValues(hidden, source, context, p);
        return hidden;
    }
}
