package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.DialogValidation;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oDialogValidation;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import org.springframework.stereotype.Component;

/**
 * Компиляция валидации с диалогом выбора
 */
@Component
public class DialogValidationCompiler extends InvocationValidationCompiler<DialogValidation, N2oDialogValidation> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDialogValidation.class;
    }

    @Override
    public DialogValidation compile(N2oDialogValidation source, CompileContext<?, ?> context, CompileProcessor p) {
        DialogValidation validation = new DialogValidation();
        compileInvocationValidation(validation, source, p);

        N2oDialog n2oDialog = new N2oDialog(source.getId());
        n2oDialog.setDescription(p.resolveJS(source.getMessage()));
        n2oDialog.setTitle(p.resolveJS(source.getTitle()));
        n2oDialog.setToolbar(source.getToolbar());
        n2oDialog.setSize(source.getSize());
        validation.setDialog(n2oDialog);

        return validation;
    }
}
