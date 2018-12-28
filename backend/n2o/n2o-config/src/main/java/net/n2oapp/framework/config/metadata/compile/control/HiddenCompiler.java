package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oHidden;
import net.n2oapp.framework.api.metadata.meta.control.Hidden;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция компонента hidden (Скрытый компонент ввода)
 */
@Component
public class HiddenCompiler extends StandardFieldCompiler<Hidden, N2oHidden>{
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oHidden.class;
    }

    @Override
    public StandardField<Hidden> compile(N2oHidden source, CompileContext<?, ?> context, CompileProcessor p) {
        Hidden field = new Hidden();
        field.setControlSrc(p.cast(field.getControlSrc(), p.resolve(property("n2o.api.control.hidden.src"), String.class)));
        StandardField<Hidden> hiddenField = compileStandardField(field, source, context, p);
        hiddenField.setVisible(false);
        return hiddenField;
    }
}
