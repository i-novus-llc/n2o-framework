package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Клиентская модель круговой диаграммы
 */
public class PieChart extends ChartWidgetComponent<PieChartWidgetComponent> {
    public PieChart() {
        super(new PieChartWidgetComponent());
    }

    @JsonProperty("pie")
    @Override
    public PieChartWidgetComponent getComponent() {
        return super.getComponent();
    }
}
