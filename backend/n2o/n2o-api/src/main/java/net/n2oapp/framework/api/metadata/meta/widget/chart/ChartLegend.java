package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель легенды на графике/диаграмме
 */
@Getter
@Setter
public class ChartLegend implements Serializable {
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
}
