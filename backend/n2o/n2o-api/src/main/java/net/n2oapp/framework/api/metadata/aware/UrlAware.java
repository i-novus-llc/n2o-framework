package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

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

    Target getTarget();

    void setTarget(Target target);

    Map<String, ModelLink> getPathMapping();
    void setPathMapping(Map<String, ModelLink> pathMapping);

    Map<String, ModelLink> getQueryMapping();
    void setQueryMapping(Map<String, ModelLink> queryMapping);
}
