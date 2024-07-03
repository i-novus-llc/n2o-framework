package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Валидатор InvokeAction
 */
@Component
public class InvokeActionValidator extends AbstractMetaActionValidator<N2oInvokeAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }

    @Override
    public void validate(N2oInvokeAction source, SourceProcessor p) {
        super.validate(source, p);
        if (isBlank(source.getOperationId()))
            throw new N2oMetadataValidationException("В действии <invoke> не указан идентификатор операции 'operation-id'");

        ValidationUtils.checkForExistsObject(source.getObjectId(),
                String.format("Действие <invoke operation-id=%s>", getIdOrEmptyString(source.getOperationId())), p);
    }
}
