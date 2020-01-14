package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель элемента круговой диаграммы
 */
@Getter
@Setter
public class PieChartItem extends AbstractChartItem {
    @JsonProperty
    private Integer cx;
    @JsonProperty
    private Integer cy;
    @JsonProperty
    private Integer innerRadius;
    @JsonProperty
    private Integer outerRadius;
    @JsonProperty
    private Integer startAngle;
    @JsonProperty
    private Integer endAngle;
    @JsonProperty
    private String nameKey;
    @JsonProperty
    private String dataKey;
    @JsonProperty("fill")
    private String color;
}
