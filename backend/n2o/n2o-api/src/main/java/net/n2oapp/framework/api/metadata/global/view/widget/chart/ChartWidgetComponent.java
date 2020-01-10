package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;

/**
 * Клиентская модель компонента диаграммы
 */
@Getter
@Setter
public class ChartWidgetComponent extends WidgetComponent {
    @JsonProperty
    private Boolean fetchOnInit;
    @JsonProperty
    private ChartType type;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
}
