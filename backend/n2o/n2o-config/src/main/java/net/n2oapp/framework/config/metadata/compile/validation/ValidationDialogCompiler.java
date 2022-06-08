package net.n2oapp.framework.config.metadata.compile.validation;

import net.n2oapp.framework.api.data.validation.ValidationDialog;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationDialog;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import org.springframework.stereotype.Component;

/**
 * Компиляция валидации с диалогом выбора
 */
@Component
public class ValidationDialogCompiler extends InvocationValidationCompiler<ValidationDialog, N2oValidationDialog> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oValidationDialog.class;
    }

    @Override
    public ValidationDialog compile(N2oValidationDialog source, CompileContext<?, ?> context, CompileProcessor p) {
        ValidationDialog validation = new ValidationDialog();
        compileInvocationValidation(validation, source, p);

        N2oDialog n2oDialog = new N2oDialog(source.getId());
        n2oDialog.setDescription(source.getMessage());
        n2oDialog.setTitle(source.getTitle());
        n2oDialog.setToolbar(source.getToolbar());
        n2oDialog.setSize(source.getSize());
        validation.setDialog(n2oDialog);

        return validation;
    }
}
