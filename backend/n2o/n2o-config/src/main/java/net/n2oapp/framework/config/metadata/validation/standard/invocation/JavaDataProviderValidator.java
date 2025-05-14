package net.n2oapp.framework.config.metadata.validation.standard.invocation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор java провайдера данных
 */
@Component
public class JavaDataProviderValidator extends TypedMetadataValidator<N2oJavaDataProvider> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oJavaDataProvider.class;
    }

    @Override
    public void validate(N2oJavaDataProvider provider, SourceProcessor p) {
        InvocationScope invocationScope = p.getScope(InvocationScope.class);

        if (provider.getMethod() == null) {
            if (invocationScope.getQueryId() != null) {
                throw new N2oMetadataValidationException(String.format("В <java> провайдере выборки %s не указан атрибут 'method'",
                        ValidationUtils.getIdOrEmptyString(invocationScope.getQueryId())));
            } else {
                if (invocationScope.getOperationId() != null) {
                    if (invocationScope.getValidationId() == null) {
                        throw new N2oMetadataValidationException(String.format("В <java> провайдере операции %s объекта %s не указан атрибут 'method'",
                                ValidationUtils.getIdOrEmptyString(invocationScope.getOperationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                    } else {
                        throw new N2oMetadataValidationException(String.format("В <java> провайдере валидации %s операции %s объекта %s не указан атрибут 'method'",
                                ValidationUtils.getIdOrEmptyString(invocationScope.getValidationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getOperationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                    }
                } else if (invocationScope.getOperationId() == null && invocationScope.getValidationId() != null) {
                    throw new N2oMetadataValidationException(String.format("В <java> провайдере валидации %s объекта %s не указан атрибут 'method'",
                            ValidationUtils.getIdOrEmptyString(invocationScope.getValidationId()),
                            ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                }
            }
        }
    }
}
