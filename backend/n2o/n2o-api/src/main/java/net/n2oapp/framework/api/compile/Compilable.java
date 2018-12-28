package net.n2oapp.framework.api.compile;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;

import java.io.Serializable;

/**
 * Собранный объект
 * @param <T> Исходный объект
 * @param <C> Контекст сборки
 */
@Deprecated
public interface Compilable<T,C> extends Serializable, IdAware {
    /**
     * Собрать объект из исходника
     * @param source Исходный объект
     * @param compiler Компилятор
     * @param context Контекст сборки
     */
    void compile(T source, N2oCompiler compiler, C context);

}
