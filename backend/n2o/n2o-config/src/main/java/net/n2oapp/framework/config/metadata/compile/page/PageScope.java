package net.n2oapp.framework.config.metadata.compile.page;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
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
    private Map<String, String> widgetIdSourceDatasourceMap = new HashMap<>();
    /**
     * Список идентификаторов таб регионов
     */
    @Deprecated
    private Set<String> tabIds;
}
