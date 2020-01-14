package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента линейного графика
 */
@Getter
@Setter
public class LineChartWidgetComponent extends AbstractChartWidgetComponent {
    @JsonProperty
    private String dataKey;
    @JsonProperty("fill")
    private String color;
}
