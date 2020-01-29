package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

/**
 * Клиентская модель компонента диаграммы
 */
@Getter
@Setter
public class ChartWidgetComponent<T extends AbstractChartItem> extends WidgetComponent {
    private String src;
    @JsonProperty
    private ChartType type;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;

    public ChartWidgetComponent() {
    }
}
