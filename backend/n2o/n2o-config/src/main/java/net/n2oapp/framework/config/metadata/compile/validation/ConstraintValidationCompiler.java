package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraintValidation;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция валидации ограничений полей
 */
@Component
public class ConstraintValidationCompiler extends InvocationValidationCompiler<ConstraintValidation, N2oConstraintValidation> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConstraintValidation.class;
    }

    @Override
    public ConstraintValidation compile(N2oConstraintValidation source, CompileContext<?, ?> context, CompileProcessor p) {
        ConstraintValidation validation = new ConstraintValidation();
        compileInvocationValidation(validation, source, p);
        validation.setSeverity(castDefault(source.getSeverity(), SeverityTypeEnum.DANGER));

        return validation;
    }
}
