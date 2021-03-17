package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

import static net.n2oapp.framework.api.exception.SeverityType.danger;
import static net.n2oapp.framework.api.exception.SeverityType.warning;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Базовая компиляция валидации
 */
public abstract class BaseValidationCompiler<D extends Validation, S extends N2oValidation> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void compileValidation(D compiled, S source) {
        compiled.setId(source.getId());
        compiled.setFieldId(source.getFieldId());
        compiled.setSide(source.getSide());
        compiled.setMessage(source.getMessage());
        resolveEnabled(compiled, source);
        if (danger.equals(source.getSeverity()) || warning.equals(source.getSeverity()))
            compiled.setMoment(castDefault(source.getServerMoment(), N2oValidation.ServerMoment.beforeOperation));
        else
            compiled.setMoment(castDefault(source.getServerMoment(), N2oValidation.ServerMoment.afterSuccessOperation));
    }

    private void resolveEnabled(D compiled, S source) {
        if (source.getEnabled() == null) return;
        if (source.getEnabled().startsWith("{") && source.getEnabled().endsWith("}"))
            compiled.addEnablingCondition(source.getEnabled());
        else
            compiled.setEnabled(Boolean.valueOf(source.getEnabled()));
    }
}
