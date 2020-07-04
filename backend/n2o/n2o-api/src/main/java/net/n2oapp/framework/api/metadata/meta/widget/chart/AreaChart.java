package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская модель диаграммы-области
 */
public class AreaChart extends StandardChartWidgetComponent<AreaChartItem> {
    public AreaChart() {
        super(new ArrayList<>());
    }

    @JsonProperty("areas")
    @Override
    public List<AreaChartItem> getItems() {
        return super.getItems();
    }
}
