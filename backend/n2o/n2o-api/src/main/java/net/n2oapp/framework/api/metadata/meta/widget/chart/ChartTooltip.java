package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель подсказки на графике/диаграмме
 */
@Getter
@Setter
public class ChartTooltip implements Compiled {
    @JsonProperty
    private String separator;
}
