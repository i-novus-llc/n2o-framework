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
    @Deprecated
    private String dependsOnWidgetId;
    @Deprecated
    private String dependsOnQueryId;
    private String clientWidgetId;
    private String widgetId;
    private String queryId;
    private String datasourceId;
    /**
     * В версии widget-4.0 и более ранних можно было задать route у виджета, в widget-5.0 уже нельзя, старый route
     * переезжает внутрь action. В скоупе он необходим для получения в трансформера action
     */
    private String oldRoute;
    /**
     * Сохранение в родительском пути идентификатора выбранной записи после возврата из open-page
     */
    private Boolean hasIdInParentRoute;
}
