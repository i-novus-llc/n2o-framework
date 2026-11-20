package net.n2oapp.framework.config.metadata.validation.standard.invocation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.InvocationScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Валидатор mongodb провайдера данных
 */
@Component
public class MongoDbDataProviderValidator extends TypedMetadataValidator<N2oMongoDbDataProvider> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMongoDbDataProvider.class;
    }

    @Override
    public void validate(N2oMongoDbDataProvider provider, SourceProcessor p) {
        if (!StringUtils.isBlank(provider.getCollectionName())) {
            return;
        }
        InvocationScope invocationScope = p.getScope(InvocationScope.class);

        if (invocationScope == null) {
            throw new N2oMetadataValidationException("В \"<mongodb>\" провайдере не указан атрибут 'collection-name'");
        } else {
            if (invocationScope.getQueryId() != null) {
                throw new N2oMetadataValidationException(String.format("В \"<mongodb>\" провайдере выборки %s не указан атрибут 'collection-name'",
                        ValidationUtils.getIdOrEmptyString(invocationScope.getQueryId())));
            } else {
                if (invocationScope.getOperationId() != null) {
                    if (invocationScope.getValidationId() == null) {
                        throw new N2oMetadataValidationException(String.format("В \"<mongodb>\" провайдере операции %s объекта %s не указан атрибут 'collection-name'",
                                ValidationUtils.getIdOrEmptyString(invocationScope.getOperationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                    } else {
                        throw new N2oMetadataValidationException(String.format("В \"<mongodb>\" провайдере валидации %s операции %s объекта %s не указан атрибут 'collection-name'",
                                ValidationUtils.getIdOrEmptyString(invocationScope.getValidationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getOperationId()),
                                ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                    }
                } else if (invocationScope.getValidationId() != null) {
                    throw new N2oMetadataValidationException(String.format("В \"<mongodb>\" провайдере валидации %s объекта %s не указан атрибут 'collection-name'",
                            ValidationUtils.getIdOrEmptyString(invocationScope.getValidationId()),
                            ValidationUtils.getIdOrEmptyString(invocationScope.getObjectId())));
                }
            }
        }
    }
}