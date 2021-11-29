package net.n2oapp.framework.config.metadata.compile.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.config.util.CompileUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Информация о странице при сборке внутренних метаданных
 */
@Getter
@Setter
public class PageScope implements Serializable {
    private String pageId;
    private String objectId;
    private String resultWidgetId;
    private Set<String> widgetIds;
    private Map<String, String> widgetIdQueryIdMap;
    private Map<String, String> widgetIdSourceDatasourceMap;
    //fixme избавиться со временем возможно, сейчас нужна потому что много где есть ссылки на widgetId , которые превратились в datasource
    private Map<String, String> widgetIdClientDatasourceMap;
    /**
     * Список идентификаторов таб регионов
     */
    private Set<String> tabIds;
    //необходим только для валидации
    //todo убрать как только перейдем к отдельным datasource
    private Map<String, DatasourceValue> datasourceValueMap = new HashMap<>();

    public String getGlobalWidgetId(String localWidgetId) {
        return CompileUtil.generateWidgetId(pageId, localWidgetId);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DatasourceValue {
        private String queryId;
        private String objectId;
    }
}
