package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
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
        List<AbstractParameter> inParams = new ArrayList<>();
        if (source.getInFields() != null)
            for (AbstractParameter parameter : source.getInFields())
                inParams.add(parameter instanceof ObjectSimpleField ?
                        new ObjectSimpleField((ObjectSimpleField) parameter) :
                        new ObjectReferenceField((ObjectReferenceField) parameter));
        validation.setInParametersList(inParams);

        //out
        List<ObjectSimpleField> outParams = new ArrayList<>();
        if (source.getOutFields() != null)
            for (ObjectSimpleField parameter : source.getOutFields())
                outParams.add(new ObjectSimpleField(parameter));
        ObjectSimpleField resultParam = new ObjectSimpleField();

        resultParam.setId(CompiledObject.VALIDATION_RESULT_PARAM);
        resultParam.setMapping(source.getResult());
        outParams.add(resultParam);

        validation.setOutParametersList(outParams);
        validation.setInvocation(source.getN2oInvocation());
        return validation;
    }
}
