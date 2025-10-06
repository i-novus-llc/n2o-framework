package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oEmailField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.masked.EmailField;
import org.springframework.stereotype.Component;

/**
 * Компиляция поля {@code <email>}
 */
@Component
public class EmailFieldCompiler extends MaskedFieldCompiler<EmailField, N2oEmailField> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.email.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oEmailField.class;
    }

    @Override
    public StandardField<EmailField> compile(N2oEmailField source, CompileContext<?, ?> context, CompileProcessor p) {
        EmailField field = new EmailField();
        return compileMaskedField(field, source, context, p);
    }
}
