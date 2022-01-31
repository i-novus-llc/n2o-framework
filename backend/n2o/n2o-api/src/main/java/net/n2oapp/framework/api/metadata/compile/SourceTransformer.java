package net.n2oapp.framework.api.metadata.compile;

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
     * @param p      Процессор исходных метаданных
     * @return Трансформированные исходные метаданные
     */
    S transform(S source, SourceProcessor p);

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
