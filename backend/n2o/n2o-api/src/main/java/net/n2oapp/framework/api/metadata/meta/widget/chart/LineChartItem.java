package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель линии линейного графика
 */
@Getter
@Setter
public class LineChartItem extends AbstractChartItem {
    @JsonProperty
    private String dataKey;
    @JsonProperty
    private ChartLineType type;
    @JsonProperty("stroke")
    private String color;
    @JsonProperty
    private Boolean label;
}
