package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;

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
    D bind(D compiled, BindProcessor p);
}
