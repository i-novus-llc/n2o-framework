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
    @JsonProperty("dataKey")
    private String fieldId;
    @JsonProperty
    private String label;
    @JsonProperty("type")
    private ChartLineType lineType;
    @JsonProperty("fill")
    private String color;
    @JsonProperty("stroke")
    private String strokeColor;
    @JsonProperty
    private Boolean hasLabel;
}
