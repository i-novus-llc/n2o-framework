package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;

/**
 * Фабрика слияний метаданных в одну {@link SourceMerger}
 */
public interface SourceMergerFactory extends MetadataFactory<SourceMerger> {
    /**
     * Заменить свойства исходной метаданной значениями перекрывающей метаданной, если они не пусты
     *
     * @param source   Исходная метаданная
     * @param override Перекрывающая метаданная
     * @return Исходная метаданная с перекрытыми свойствами
     */
    <S> S merge(S source, S override);
}
