package net.n2oapp.framework.api.metadata.compile.building;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Конструктор экземпляров собранной метаданной
 * @param <D> Тип собранной метаданной
 */
public interface CompileConstructor<D extends Compiled, S> {
    /**
     * Создать эксземпляр собранной метаданной
     * @param source Исходная метаданная
     * @return Новый экземпляр собранной метаданной
     */
    D construct(S source);
}
