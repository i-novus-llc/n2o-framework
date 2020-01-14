package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента диаграммы-области
 */
@Getter
@Setter
public class AreaChartWidgetComponent extends AbstractChartWidgetComponent {
    @JsonProperty
    private String stackId;
    @JsonProperty
    private String dataKey;
    @JsonProperty("fill")
    private String color;
}
