package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель подсказки на графике/диаграмме
 */
@Getter
@Setter
public class ChartTooltip implements Serializable {
    @JsonProperty
    private String separator;
}
