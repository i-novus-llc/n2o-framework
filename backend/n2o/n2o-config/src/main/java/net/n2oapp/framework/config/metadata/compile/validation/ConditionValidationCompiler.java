package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationCondition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.util.FileSystemUtil.getContentByUri;

/**
 * Компиляция валидации условия значений полей
 */
@Component
public class ConditionValidationCompiler extends BaseValidationCompiler<ConditionValidation, N2oValidationCondition> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oValidationCondition.class;
    }

    @Override
    public ConditionValidation compile(N2oValidationCondition source, CompileContext<?, ?> context, CompileProcessor p) {
        ConditionValidation validation = new ConditionValidation();
        compileValidation(validation, source);
        validation.setSeverity(source.getSeverity());
        validation.setExpression(ScriptProcessor.resolveFunction(p.cast(source.getExpression(), getContentByUri(source.getSrc()))));
        validation.setExpressionOn(source.getExpressionOn());
        return validation;
    }
}
