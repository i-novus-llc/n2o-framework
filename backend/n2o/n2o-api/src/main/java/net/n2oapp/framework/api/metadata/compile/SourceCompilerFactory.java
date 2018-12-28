package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Фабрика сборщиков метаданных {@link SourceCompiler}
 */
public interface SourceCompilerFactory extends MetadataFactory<SourceCompiler> {

    /**
     * Собрать объект
     * @param source Исходный объект
     * @param context Контекст сборки
     * @param p Процессор сборки
     * @return Собранный объект
     */
    <D extends Compiled, S, C extends CompileContext<?, ?>> D compile(S source, C context, CompileProcessor p);
}
