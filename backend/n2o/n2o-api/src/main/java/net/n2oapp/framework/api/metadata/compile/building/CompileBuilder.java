package net.n2oapp.framework.api.metadata.compile.building;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Сброщик метаданных
 */
@FunctionalInterface
public interface CompileBuilder<D, S> {

    /**
     * Собрать звено цепи
     * @param p Процессор сборки
     */
    void build(BuildProcessor<D, S> p);
}
