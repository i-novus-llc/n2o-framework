package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Map;

/**
 * Связывание метаданных с данными
 * @param <D> Собранные метаданные
 */
@FunctionalInterface
public interface MetadataBinder<D extends Compiled> {
    /**
     * Связать метаданные с данными
     * @param compiled Собранные метаданные
     * @param p Процессор связывания
     * @return Связанные метаданные с данными
     */
    D bind(D compiled, CompileProcessor p);
}
