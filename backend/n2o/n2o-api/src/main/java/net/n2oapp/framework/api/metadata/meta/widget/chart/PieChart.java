package net.n2oapp.framework.api.metadata.meta.widget.chart;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Клиентская модель круговой диаграммы
 */
public class PieChart extends SimpleChartWidgetComponent<PieChartItem> {
    public PieChart() {
        super(new PieChartItem());
    }

    @JsonProperty("pie")
    @Override
    public PieChartItem getComponent() {
        return super.getComponent();
    }
}
