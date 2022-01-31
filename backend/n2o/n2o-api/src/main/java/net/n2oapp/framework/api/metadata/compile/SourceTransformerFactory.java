package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;

/**
 * Фабрика трансформаторов исходных метаданных {@link SourceTransformer}
 */
public interface SourceTransformerFactory extends MetadataFactory<SourceTransformer<?>> {

    /**
     * Трансформировать исходные метаданные
     *
     * @param source Исходные метаданные
     * @param p      Процессор исходных метаданных
     * @return Трансформированные исходные метаданные
     */
    <S> S transform(S source, SourceProcessor p);

}
