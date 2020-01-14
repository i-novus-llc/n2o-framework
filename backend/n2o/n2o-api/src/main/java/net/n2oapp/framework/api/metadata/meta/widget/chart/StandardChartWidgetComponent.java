package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель компонента стандартной диаграммы
 */
@Getter
@Setter
public class StandardChartWidgetComponent<T extends AbstractChartItem> extends ChartWidgetComponent {
     protected List<T> items;
     @JsonProperty("XAxis")
     private ChartAxis xAxis;
     @JsonProperty("YAxis")
     private ChartAxis yAxis;
     @JsonProperty("cartesianGrid")
     private ChartGrid grid;
     @JsonProperty("tooltip")
     private ChartTooltip tooltip;
     @JsonProperty("legend")
     private ChartLegend legend;

     public StandardChartWidgetComponent(List<T> items) {
          this.items = items;
     }

     public void addItem(T item) {
          items.add(item);
     }
}
