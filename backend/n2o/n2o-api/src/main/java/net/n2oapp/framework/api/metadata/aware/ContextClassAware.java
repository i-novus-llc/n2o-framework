package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.compile.CompileContext;

/**
 * Знание о классе контекста сборки метаданной
 */
public interface ContextClassAware {
    Class<? extends CompileContext<?, ?>> getContextClass();
}
