package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание об имени css класса
 */
public interface CssClassAware {
    String getCssClass();
    void setCssClass(String cssClass);

    String getStyle();
    void setStyle(String style);
}
