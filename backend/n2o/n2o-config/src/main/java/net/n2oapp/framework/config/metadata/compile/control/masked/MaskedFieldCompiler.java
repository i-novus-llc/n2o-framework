package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.masked.N2oMaskedField;
import net.n2oapp.framework.api.metadata.meta.control.masked.MaskedControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.config.metadata.compile.control.StandardFieldCompiler;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция поля ввода с маской
 */
public abstract class MaskedFieldCompiler<D extends MaskedControl, S extends N2oMaskedField> extends StandardFieldCompiler<D, S> {

    protected StandardField<D> compileMaskedField(D control, S source, CompileContext<?, ?> context, CompileProcessor p) {
        control.setInvalidText(castDefault(source.getInvalidText(),
                () -> p.getMessage(("n2o.api.control.masked.invalid_text"), String.class)));
        return compileStandardField(control, source, context, p);
    }
}
