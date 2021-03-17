package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.InvocationValidation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.ArrayList;
import java.util.List;

public abstract class InvocationValidationCompiler<D extends InvocationValidation, S extends N2oInvocationValidation>
        extends BaseValidationCompiler<D, S> {

    protected void compileInvocationValidation(D compiled, S source) {
        compileValidation(compiled, source);
        compiled.setId(source.getId());

        //in
        List<AbstractParameter> inParams = new ArrayList<>();
        if (source.getInFields() != null)
            for (AbstractParameter parameter : source.getInFields())
                inParams.add(parameter instanceof ObjectSimpleField ?
                        new ObjectSimpleField((ObjectSimpleField) parameter) :
                        new ObjectReferenceField((ObjectReferenceField) parameter));
        compiled.setInParametersList(inParams);

        //out
        List<ObjectSimpleField> outParams = new ArrayList<>();
        if (source.getOutFields() != null)
            for (ObjectSimpleField parameter : source.getOutFields())
                outParams.add(new ObjectSimpleField(parameter));

        ObjectSimpleField resultParam = new ObjectSimpleField();
        resultParam.setId(CompiledObject.VALIDATION_RESULT_PARAM);
        resultParam.setMapping(source.getResult());
        outParams.add(resultParam);

        compiled.setOutParametersList(outParams);
        compiled.setInvocation(source.getN2oInvocation());
    }
}
