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
    private String clientWidgetId;
    private String widgetId;
    private String datasourceId;
}
