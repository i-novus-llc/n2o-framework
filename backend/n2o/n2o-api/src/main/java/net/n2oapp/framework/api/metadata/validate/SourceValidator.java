package net.n2oapp.framework.api.metadata.validate;

import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

/**
 * Валидатор метаданных
 * @param <S> Тип метаданной
 */
@FunctionalInterface
public interface SourceValidator<S> {
    /**
     * Валидировать исходную метаданную
     * @param source Исходная метаданная
     * @throws N2oMetadataValidationException Иключение при ошибке валидации
     */
    void validate(S source, SourceProcessor p);
}
