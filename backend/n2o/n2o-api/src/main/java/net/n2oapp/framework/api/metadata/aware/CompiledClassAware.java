package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Знание о классе собранной метаданной
 */
public interface CompiledClassAware {
    Class<? extends Compiled> getCompiledClass();
}
