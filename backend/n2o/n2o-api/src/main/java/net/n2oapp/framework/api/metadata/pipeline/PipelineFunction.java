package net.n2oapp.framework.api.metadata.pipeline;

import java.util.function.Function;

/**
 * Функция конвейера сборки метаданных
 * @param <P> Тип конвейерной сборки
 */
public interface PipelineFunction<P extends Pipeline> extends Function<PipelineSupport, P> {
}
