package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Клиентская модель линейного графика
 */
public class LineChart extends StandardChartWidgetComponent<LineChartWidgetComponent> {
    public LineChart() {
        super(new LineChartWidgetComponent());
    }

    @JsonProperty("lines")
    @Override
    public LineChartWidgetComponent getComponent() {
        return (LineChartWidgetComponent) super.getComponent();
    }
}
