package net.n2oapp.framework.api.metadata.validate;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;

/**
 * Валидатор исходных метаданных
 * @param <S> Тип исходной метаданной
 */
@FunctionalInterface
public interface SourceValidator<S> {
    /**
     * Валидировать исходную метаданную
     * @param source Исходная метаданная
     * @throws N2oMetadataValidationException Иключение при ошибке валидации
     */
    void validate(S source) throws N2oMetadataValidationException;
}
