package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
        if (StringUtils.isBlank(source.getOperationId()))
            throw new N2oMetadataValidationException("В действии <invoke> не указан идентификатор операции 'operation-id'");

        if (source.getObjectId() != null)
            p.checkForExists(source.getObjectId(), N2oObject.class,
                    String.format("Действие <invoke operation-id=%s> ссылается на несуществующий объект %s",
                            ValidationUtils.getIdOrEmptyString(source.getOperationId()),
                            ValidationUtils.getIdOrEmptyString(source.getObjectId())));
    }
}
