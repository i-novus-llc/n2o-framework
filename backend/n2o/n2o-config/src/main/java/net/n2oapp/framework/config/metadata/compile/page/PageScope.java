package net.n2oapp.framework.config.metadata.compile.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;
import java.util.Set;

/**
 * Информация о странице при сборке внутренних метаданных
 */
@Getter
@Setter
public class PageScope {
    private String pageId;
    private String objectId;
    private String resultWidgetId;
    @Deprecated
    private Set<String> widgetIds;
    @Deprecated
    private Map<String, String> widgetIdQueryIdMap;
    private Map<String, String> widgetIdSourceDatasourceMap = new StrictMap<>();
    //fixme избавиться со временем возможно, сейчас нужна потому что много где есть ссылки на widgetId , которые превратились в datasource
    @Deprecated
    private Map<String, String> widgetIdClientDatasourceMap;
    /**
     * Список идентификаторов таб регионов
     */
    @Deprecated
    private Set<String> tabIds;
}
