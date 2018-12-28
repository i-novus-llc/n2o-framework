package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о реализации метаданной
 */
public interface SrcAware {
    /**
     * Получить реализацию метаданной
     * @return Реализация метаданной
     */
    String getSrc();

    /**
     * Установить реализацию метаданной
     * @param src Реализация метаданной
     */
    void setSrc(String src);
}
