package net.n2oapp.framework.api.metadata.compile;

/**
 * Трансформатор исходных метаданных
 * @param <S> Тип исходных метаданных
 */
@FunctionalInterface
public interface SourceTransformer<S> {
    /**
     * Трансформировать исходные метаданные
     * @param source Исходные метаданные
     * @return Трансформированные исходные метаданные
     */
    S transform(S source);
}
