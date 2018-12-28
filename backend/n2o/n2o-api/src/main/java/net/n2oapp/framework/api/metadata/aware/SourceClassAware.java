package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Source;

/**
 * Знание о классе исходной метаданной*
 */
public interface SourceClassAware {
    /**
     * Получить класс исходной метаданной
     * @return Класс исходной метаданной
     */
    Class<? extends Source> getSourceClass();
}
