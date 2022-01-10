package net.n2oapp.framework.config.metadata.compile.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
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
    //необходим только для валидации
    //todo убрать как только перейдем к отдельным datasource
    @Deprecated
    private Map<String, DatasourceValue> datasourceValueMap = new HashMap<>();

    public String getGlobalWidgetId(String localWidgetId) {
        return CompileUtil.generateWidgetId(pageId, localWidgetId);
    }

    public String getClientDatasourceId(String localDatasourceId) {
        return  CompileUtil.generateWidgetId(pageId, localDatasourceId);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DatasourceValue {
        private String queryId;
        private String objectId;
    }
}
