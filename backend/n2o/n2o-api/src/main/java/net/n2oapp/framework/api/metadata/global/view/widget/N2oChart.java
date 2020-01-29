package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;

/**
 * Исходная модель виджета-диаграммы
 */
@Getter
@Setter
public class N2oChart extends N2oWidget {
    private Integer width;
    private Integer height;
    private N2oAbstractChart component;
}
