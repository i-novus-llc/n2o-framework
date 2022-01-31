package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Знание о классе собранной метаданной
 */
public interface CompiledClassAware {
    /**
     * Получить класс собранной метаданной
     * @return Класс собранной метаданной
     */
    Class<? extends Compiled> getCompiledClass();
}
