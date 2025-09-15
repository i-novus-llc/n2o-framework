package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oUuidField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.UuidField;
import org.springframework.stereotype.Component;

/**
 * Компиляция поля {@code <uuid>}
 */
@Component
public class UuidFieldCompiler extends MaskedFieldCompiler<UuidField, N2oUuidField> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.uuid.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oUuidField.class;
    }

    @Override
    public StandardField<UuidField> compile(N2oUuidField source, CompileContext<?, ?> context, CompileProcessor p) {
        UuidField field = new UuidField();
        return compileMaskedField(field, source, context, p);
    }
}
