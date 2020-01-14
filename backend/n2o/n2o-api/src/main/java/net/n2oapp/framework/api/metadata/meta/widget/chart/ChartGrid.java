package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель сетки графика/диаграммы
 */
@Getter
@Setter
public class ChartGrid implements Serializable {
    @JsonProperty
    private Integer x;
    @JsonProperty
    private Integer y;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
}
