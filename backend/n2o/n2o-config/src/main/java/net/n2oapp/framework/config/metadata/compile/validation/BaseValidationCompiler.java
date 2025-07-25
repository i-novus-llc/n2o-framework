package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;

import static net.n2oapp.framework.api.exception.SeverityTypeEnum.DANGER;
import static net.n2oapp.framework.api.exception.SeverityTypeEnum.WARNING;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Базовая компиляция валидации
 */
public abstract class BaseValidationCompiler<D extends Validation, S extends N2oValidation> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void compileValidation(D compiled, S source, CompileProcessor p) {
        compiled.setId(source.getId());
        compiled.setFieldId(source.getFieldId());
        compiled.setSide(source.getSide());
        compiled.setMessage(castDefault(source.getMessage(), () -> p.getMessage("n2o.validation.message")));
        compiled.setMessageTitle(source.getTitle());
        compiled.setJsonMessage((String) ScriptProcessor.resolveExpression(source.getMessage()));
        resolveEnabled(compiled, source, p);
        if (DANGER.equals(source.getSeverity()) || WARNING.equals(source.getSeverity()))
            compiled.setMoment(castDefault(source.getServerMoment(), N2oValidation.ServerMomentEnum.BEFORE_OPERATION));
        else
            compiled.setMoment(castDefault(source.getServerMoment(), N2oValidation.ServerMomentEnum.AFTER_SUCCESS_OPERATION));
    }

    private void resolveEnabled(D compiled, S source, CompileProcessor p) {
        if (source.getEnabled() == null) return;
        String expression = p.resolveJS(source.getEnabled());
        if (StringUtils.isJs(expression)) {
            compiled.addEnablingCondition(StringUtils.unwrapJs(expression));
        } else
            compiled.setEnabled(Boolean.valueOf(expression));
    }
}
