package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель области диаграммы-области
 */
@Getter
@Setter
public class AreaChartItem extends AbstractChartItem {
    @JsonProperty
    private String dataKey;
    @JsonProperty("type")
    private ChartLineType lineType;
    @JsonProperty("fill")
    private String color;
    @JsonProperty
    private String stroke;
    @JsonProperty
    private Boolean label;
}
