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
    @JsonProperty
    private String dataKey;
    @JsonProperty
    private String orientation;
    @JsonProperty
    private Boolean label;
}
