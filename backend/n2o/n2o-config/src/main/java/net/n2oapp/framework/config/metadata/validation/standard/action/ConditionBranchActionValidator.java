package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkCloseInMultiAction;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailActionNotExist;

/**
 * Валидатор действия if-else
 */
@Component
public class ConditionBranchActionValidator extends TypedMetadataValidator<N2oConditionBranch> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oConditionBranch.class;
    }

    @Override
    public void validate(N2oConditionBranch source, SourceProcessor p) {
        if (source.getActions() == null) return;
        checkOnFailActionNotExist(source.getActions(), "операторе 'if-else'");
        checkCloseInMultiAction(source.getActions());
    }
}