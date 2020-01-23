package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель оси координат на графике/диаграмме
 */
@Getter
@Setter
public class ChartAxis implements Serializable {
    @JsonProperty("dataKey")
    private String fieldId;
    @JsonProperty("orientation")
    private String position;
    @JsonProperty("label")
    private Boolean hasLabel;
}
