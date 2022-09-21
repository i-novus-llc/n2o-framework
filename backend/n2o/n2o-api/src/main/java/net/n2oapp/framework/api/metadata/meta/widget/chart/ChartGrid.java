package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель сетки графика/диаграммы
 */
@Getter
@Setter
public class ChartGrid implements Compiled {
    @JsonProperty("strokeDasharray")
    private String strokeDashArray;
    @JsonProperty
    private Boolean horizontal;
    @JsonProperty
    private Boolean vertical;
}
