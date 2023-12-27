package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConditionValidation;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция валидации условия значений полей
 */
@Component
public class ConditionValidationCompiler extends BaseValidationCompiler<ConditionValidation, N2oConditionValidation> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConditionValidation.class;
    }

    @Override
    public ConditionValidation compile(N2oConditionValidation source, CompileContext<?, ?> context, CompileProcessor p) {
        ConditionValidation validation = new ConditionValidation();
        compileValidation(validation, source, p);
        validation.setSeverity(castDefault(source.getSeverity(), SeverityType.danger));
        validation.setExpression(ScriptProcessor.resolveFunction(castDefault(source.getExpression(), () -> p.getExternalFile(source.getSrc()))));
        validation.setExpressionOn(source.getExpressionOn());
        return validation;
    }
}
