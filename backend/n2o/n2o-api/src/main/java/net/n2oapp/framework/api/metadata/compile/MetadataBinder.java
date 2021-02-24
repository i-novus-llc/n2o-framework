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

    /**
     * Подходит ли метаданная для связывания?
     * @param compiled Собранная метаданная
     * @param p Процессор связывания
     * @return true - подходит, false - не подходит
     */
    default boolean matches(D compiled, BindProcessor p) {
        return true;
    }
}
