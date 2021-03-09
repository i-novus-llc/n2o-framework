package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компиляция валидации ограничений полей
 */
@Component
public class ConstraintValidationCompiler extends BaseValidationCompiler<ConstraintValidation, N2oConstraint> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConstraint.class;
    }

    @Override
    public ConstraintValidation compile(N2oConstraint source, CompileContext<?, ?> context, CompileProcessor p) {
        ConstraintValidation validation = new ConstraintValidation();
        compileValidation(validation, source, context, p);
        validation.setId(source.getId());
        validation.setSeverity(source.getSeverity());

        //in
        List<InvocationParameter> inParams = new ArrayList<>();
        if (source.getInParameters() != null)
            for (N2oObject.Parameter parameter : source.getInParameters())
                inParams.add(new InvocationParameter(parameter));
        validation.setInParametersList(inParams);

        //out
        List<InvocationParameter> outParams = new ArrayList<>();
        if (source.getOutParameters() != null)
            for (N2oObject.Parameter parameter : source.getOutParameters())
                outParams.add(new InvocationParameter(parameter));
        InvocationParameter resultParam = new InvocationParameter();

        resultParam.setId(CompiledObject.VALIDATION_RESULT_PARAM);
        resultParam.setMapping(source.getResult());
        outParams.add(resultParam);

        validation.setOutParametersList(outParams);
        validation.setInvocation(source.getN2oInvocation());
        return validation;
    }
}
