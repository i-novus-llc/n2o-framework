package net.n2oapp.framework.config.metadata.compile.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.config.util.CompileUtil;

import java.io.Serializable;
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
    private Map<String, String> widgetIdDatasourceMap;

    public String getGlobalWidgetId(String localWidgetId) {
        return CompileUtil.generateWidgetId(pageId, localWidgetId);
    }
}
