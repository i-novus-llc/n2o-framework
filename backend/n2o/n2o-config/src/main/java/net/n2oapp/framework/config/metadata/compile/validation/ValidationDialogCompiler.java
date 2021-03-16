package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ValidationDialog;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationDialog;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компиляция валидации с диалогом выбора
 */
@Component
public class ValidationDialogCompiler extends BaseValidationCompiler<ValidationDialog, N2oValidationDialog> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oValidationDialog.class;
    }

    @Override
    public ValidationDialog compile(N2oValidationDialog source, CompileContext<?, ?> context, CompileProcessor p) {
        ValidationDialog validation = new ValidationDialog();
        compileValidation(validation, source, context, p);
        validation.setId(source.getId());

        //in
        List<AbstractParameter> inParams = new ArrayList<>();
        if (source.getInParameters() != null)
            for (AbstractParameter parameter : source.getInFields())
                inParams.add(new InvocationParameter(parameter));
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

        N2oDialog n2oDialog = new N2oDialog(source.getId());
        n2oDialog.setTitle(source.getMessage());
        n2oDialog.setToolbar(source.getToolbar());
        n2oDialog.setSize(source.getSize());
        validation.setDialog(n2oDialog);

        return validation;
    }
}
