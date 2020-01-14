package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента стандартной диаграммы
 */
@Getter
@Setter
public class StandardChartWidgetComponent<T extends AbstractChartWidgetComponent> extends ChartWidgetComponent {
     @JsonProperty("XAxis")
     private ChartCoord xAxis;
     @JsonProperty("YAxis")
     private ChartCoord yAxis;
     @JsonProperty("cartesianGrid")
     private ChartGrid grid;
     @JsonProperty("tooltip")
     private ChartTooltip tooltip;
     @JsonProperty("legend")
     private ChartLegend legend;

     public StandardChartWidgetComponent(T component) {
          this.component = component;
     }
}
