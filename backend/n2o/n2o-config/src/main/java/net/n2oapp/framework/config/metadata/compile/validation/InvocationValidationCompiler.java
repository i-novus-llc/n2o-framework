package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.InvocationValidation;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oInvocationValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.ArrayList;
import java.util.List;

public abstract class InvocationValidationCompiler<D extends InvocationValidation, S extends N2oInvocationValidation>
        extends BaseValidationCompiler<D, S> {

    protected void compileInvocationValidation(D compiled, S source, CompileProcessor p) {
        compileValidation(compiled, source, p);
        compiled.setId(source.getId());
        compiled.setInvocation(source.getN2oInvocation());
        //in
        compiled.setInParametersList(getParams(source.getInFields()));
        //out
        List<AbstractParameter> outParams = getParams(source.getOutFields());
        ObjectSimpleField resultParam = new ObjectSimpleField();
        resultParam.setId(CompiledObject.VALIDATION_RESULT_PARAM);
        resultParam.setMapping(source.getResult());
        outParams.add(resultParam);
        compiled.setOutParametersList(outParams);
    }

    private List<AbstractParameter> getParams(AbstractParameter[] parameters) {
        List<AbstractParameter> params = new ArrayList<>();
        if (parameters != null)
            for (AbstractParameter parameter : parameters)
                params.add(parameter instanceof ObjectSimpleField objectSimpleField ?
                        new ObjectSimpleField(objectSimpleField) :
                        new ObjectReferenceField((ObjectReferenceField) parameter));
        return params;
    }
}
