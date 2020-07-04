package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская модель гистограммы
 */
public class BarChart extends StandardChartWidgetComponent<BarChartItem> {
    public BarChart() {
        super(new ArrayList<>());
    }

    @JsonProperty("bars")
    @Override
    public List<BarChartItem> getItems() {
        return super.getItems();
    }
}
