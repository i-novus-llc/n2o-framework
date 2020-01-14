package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Клиентская модель диаграммы-области
 */
public class AreaChart extends StandardChartWidgetComponent<AreaChartWidgetComponent> {
    public AreaChart() {
        super(new AreaChartWidgetComponent());
    }

    @JsonProperty("areas")
    @Override
    public AreaChartWidgetComponent getComponent() {
        return (AreaChartWidgetComponent) super.getComponent();
    }
}
