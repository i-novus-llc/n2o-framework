package net.n2oapp.framework.api.metadata.validate;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

/**
 * Фабрика валидаторов исходных метаданных {@link SourceValidator}
 */
public interface SourceValidatorFactory extends MetadataFactory<SourceValidator> {

    /**
     * Валидировать исходную метаданную
     * @param source Исходная метаданная
     * @throws N2oMetadataValidationException Иключение при ошибке валидации
     */
    <S> void validate(S source, SourceProcessor p);

}
