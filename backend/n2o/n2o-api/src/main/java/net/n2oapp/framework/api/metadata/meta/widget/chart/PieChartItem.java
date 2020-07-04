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
    @JsonProperty("cx")
    private Integer centerX;
    @JsonProperty("cy")
    private Integer centerY;
    @JsonProperty
    private Integer innerRadius;
    @JsonProperty
    private Integer outerRadius;
    @JsonProperty
    private Integer startAngle;
    @JsonProperty
    private Integer endAngle;
    @JsonProperty("nameKey")
    private String nameFieldId;
    @JsonProperty("dataKey")
    private String valueFieldId;
    @JsonProperty("fill")
    private String color;
    @JsonProperty("label")
    private Boolean hasLabel;
}
