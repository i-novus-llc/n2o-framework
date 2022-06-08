package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import org.springframework.stereotype.Component;

/**
 * Компиляция валидации ограничений полей
 */
@Component
public class ConstraintValidationCompiler extends InvocationValidationCompiler<ConstraintValidation, N2oConstraint> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConstraint.class;
    }

    @Override
    public ConstraintValidation compile(N2oConstraint source, CompileContext<?, ?> context, CompileProcessor p) {
        ConstraintValidation validation = new ConstraintValidation();
        compileInvocationValidation(validation, source, p);
        validation.setSeverity(source.getSeverity());

        return validation;
    }
}
