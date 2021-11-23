package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;

/**
 * Трансформатор исходных метаданных
 *
 * @param <S> Тип исходных метаданных
 */
@FunctionalInterface
public interface SourceTransformer<S> {

    /**
     * Трансформировать исходные метаданные
     *
     * @param source Исходные метаданные
     * @param p      Процессор валидации метаданных
     * @return Трансформированные исходные метаданные
     */
    S transform(S source, ValidateProcessor p);

    /**
     * Подходит ли исходная метаданная для трансформации?
     *
     * @param source Исходные метаданные
     * @return true - подходит, false - не подходит
     */
    default boolean matches(S source) {
        return true;
    }
}
