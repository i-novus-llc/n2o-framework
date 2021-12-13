package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Информация о виджете при сборке внутренних метаданных
 */
@Getter
@Setter
public class WidgetScope implements Serializable {
    private String dependsOnWidgetId;
    private String dependsOnQueryId;
    private String clientWidgetId;
    private String widgetId;
    private String queryId;
    private String datasourceId;
    /**
     * Сохранение в родительском пути идентификатора выбранной записи после возврата из open-page
     */
    private Boolean hasIdInParentRoute;
}
