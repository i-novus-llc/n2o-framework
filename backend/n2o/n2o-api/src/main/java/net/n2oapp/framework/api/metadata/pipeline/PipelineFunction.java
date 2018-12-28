package net.n2oapp.framework.api.metadata.pipeline;

import java.util.function.Function;

/**
 * Функция конвеера сборки метаданных
 * @param <P> Тип конвеерной сборки
 */
public interface PipelineFunction<P extends Pipeline> extends Function<PipelineSupport, P> {
}
