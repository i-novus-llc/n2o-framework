package net.n2oapp.framework.config.compile.pipeline;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.config.metadata.compile.context.BaseCompileContext;

/**
 * Вспомогательный контекст сборки
 * @param <S> Тип исходной метаданной
 */
public class DummyCompileContext<S> extends BaseCompileContext<Compiled, S> {
    public DummyCompileContext(String sourceId, Class<S> sourceClass) {
        super(sourceId, sourceClass, null);
    }
}