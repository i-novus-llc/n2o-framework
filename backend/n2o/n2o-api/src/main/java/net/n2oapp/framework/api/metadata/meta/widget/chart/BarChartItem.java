package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель столбца гистограммы
 */
@Getter
@Setter
public class BarChartItem extends AbstractChartItem {
    @JsonProperty("dataKey")
    private String fieldId;
    @JsonProperty
    private String label;
    @JsonProperty("fill")
    private String color;
    @JsonProperty
    private Boolean hasLabel;
}
