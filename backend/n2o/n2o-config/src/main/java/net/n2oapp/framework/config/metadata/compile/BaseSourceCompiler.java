package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.SourceCompiler;

/**
 * Базовый класс сборок любых метаданных
 * @param <D> Тип собранной метаданной
 * @param <S> Тип исходной метаданной
 * @param <C> Тип контекста сборки
 */
public interface BaseSourceCompiler<D extends Compiled, S, C extends CompileContext<?, ?>>
        extends SourceCompiler<D, S, C>, SourceClassAware {
}
