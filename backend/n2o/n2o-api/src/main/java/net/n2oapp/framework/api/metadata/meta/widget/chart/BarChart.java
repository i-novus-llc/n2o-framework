package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Клиентская модель гистограммы
 */
public class BarChart extends StandardChartWidgetComponent<BarChartWidgetComponent> {
    public BarChart() {
        super(new BarChartWidgetComponent());
    }

    @JsonProperty("bars")
    @Override
    public BarChartWidgetComponent getComponent() {
        return (BarChartWidgetComponent) super.getComponent();
    }
}
