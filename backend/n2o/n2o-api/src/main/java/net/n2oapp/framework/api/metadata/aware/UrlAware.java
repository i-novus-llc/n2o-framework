package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание об URL
 */
public interface UrlAware {

    /**
     * Получить URL
     * @return URL
     */
    String getUrl();

    /**
     * Установить URL
     * @param url URL
     */
    void setUrl(String url);
}
