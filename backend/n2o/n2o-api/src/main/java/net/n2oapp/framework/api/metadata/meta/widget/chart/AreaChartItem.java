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
    @JsonProperty("fill")
    private String color;
}
