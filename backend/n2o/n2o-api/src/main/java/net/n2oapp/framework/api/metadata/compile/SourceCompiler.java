package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Сборщик метаданных
 *
 * @param <S> Тип исходной метаданной
 * @param <D> Тип собранной метаданной
 */
@FunctionalInterface
public interface SourceCompiler<D extends Compiled, S, C extends CompileContext<?, ?>> {
    /**
     * Собрать объект
     *
     * @param source  Исходный объект
     * @param context Контекст сборки
     * @param p       Процессор сборки
     * @return Собранный объект
     */
    D compile(S source, C context, CompileProcessor p);
}
