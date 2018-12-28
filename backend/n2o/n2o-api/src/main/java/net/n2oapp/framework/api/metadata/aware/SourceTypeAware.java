package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о типе метаданной
 */
public interface SourceTypeAware {
    /**
     * Получить тип метаданной
     *
     * @return Тип метаданной
     */
    String getSourceType();
}
