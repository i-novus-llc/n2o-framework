package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель оси координат на графике/диаграмме
 */
@Getter
@Setter
public class ChartAxis implements Compiled {
    @JsonProperty("dataKey")
    private String fieldId;
    @JsonProperty("orientation")
    private String position;
    @JsonProperty
    private Integer min;
    @JsonProperty
    private Integer max;
    @JsonProperty("label")
    private Boolean hasLabel;
}
