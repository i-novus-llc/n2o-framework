package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель простого компонента стандартной диаграммы
 */
@Getter
@Setter
public class SimpleChartWidgetComponent <T extends AbstractChartItem> extends ChartWidgetComponent {
    protected T component;

    public SimpleChartWidgetComponent(T component) {
        this.component = component;
    }
}
