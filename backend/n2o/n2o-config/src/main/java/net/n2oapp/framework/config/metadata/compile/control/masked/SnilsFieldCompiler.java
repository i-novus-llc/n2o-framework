package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oSnilsField;
import net.n2oapp.framework.api.metadata.meta.control.masked.SnilsField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

/**
 * Компиляция поля {@code <snils>}
 */
@Component
public class SnilsFieldCompiler extends MaskedFieldCompiler<SnilsField, N2oSnilsField> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.snils.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSnilsField.class;
    }

    @Override
    public StandardField<SnilsField> compile(N2oSnilsField source, CompileContext<?, ?> context, CompileProcessor p) {
        SnilsField field = new SnilsField();
        return compileMaskedField(field, source, context, p);
    }
}
