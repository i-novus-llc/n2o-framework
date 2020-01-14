package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская модель линейного графика
 */
public class LineChart extends StandardChartWidgetComponent<LineChartItem> {
    public LineChart() {
        super(new ArrayList<>());
    }

    @JsonProperty("lines")
    @Override
    public List<LineChartItem> getItems() {
        return super.getItems();
    }
}
