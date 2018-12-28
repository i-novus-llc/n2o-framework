package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Трансформатор собранных метаданных
 *
 * @param <D> Тип собранных метаданных
 * @param <C> Контекст сборки
 */
@FunctionalInterface
public interface CompileTransformer<D extends Compiled, C extends CompileContext<?, ?>> {
    /**
     * Трансформировать собранные метаданные
     *
     * @param compiled Собранные метаданные
     * @param context  Контекст сборки
     * @return Трансформированные собранные метаданные
     */
    D transform(D compiled, C context, CompileProcessor p);
}
