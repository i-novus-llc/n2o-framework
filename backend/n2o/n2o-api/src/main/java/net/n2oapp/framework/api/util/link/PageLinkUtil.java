package net.n2oapp.framework.api.util.link;

import static net.n2oapp.framework.api.util.link.GlobalLinkUtil.createLink;

/**
 * @author iryabov
 * @since 29.07.2016
 */
@Deprecated
public class PageLinkUtil {
    private static final String SELECT_MODEL_LINK = "single";
    private static final String FILTER_MODEL_LINK = "filter";
    private static final String MULTI_MODEL_LINK = "multi";
    private static final String CONTROL_MODEL_LINK = "control";

    /**
     * Создание линка на поле в основной модели контейнера
     * @param containerId id контейнера
     * @param fieldId id поля
     * @return линк на основную модель
     */
    public static String createSelectLink(String containerId, String fieldId) {
        return createLink(SELECT_MODEL_LINK, getFieldLink(containerId, fieldId));
    }

    /**
     * Создание линка на поле в модели фильтров контейнера
     * @param containerId id контейнера
     * @param fieldId id поля
     * @return линк на основную модель
     */
    public static String createFilterLink(String containerId, String fieldId) {
        return createLink(FILTER_MODEL_LINK, getFieldLink(containerId, fieldId));
    }

    /**
     * Создание линка на модель группвых операций в контейнере
     * @param containerId id контейнера
     * @param fieldId id поля
     * @return линк на модель фильтров
     */
    public static String createBulkLink(String containerId, String fieldId) {
        return createLink(MULTI_MODEL_LINK, getFieldLink(containerId, fieldId));
    }

    /**
     * Создание линка на модель контрола в контейнере
     * @param containerId id контейнера
     * @param fieldId id поля
     * @return линк на модель фильтров
     */
    public static String createControlLink(String containerId, String fieldId, String property) {
        return createLink(CONTROL_MODEL_LINK, getFieldLink(containerId.concat(".").concat(fieldId), property));
    }

    /**
     * Создание ссылки на поле контейнера
     * @param containerId id контейнера
     * @param fieldId id поля
     * @return линк на модуль групповых операций
     */
    private static String getFieldLink(String containerId, String fieldId) {
        if (fieldId != null)
            return containerId + ":" + fieldId;
        else
            return containerId;
    }

}
