package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import net.n2oapp.framework.api.metadata.control.N2oComponent;

import java.util.List;

/**
 * Исходная модель абстрактного компонента диаграммы
 */
public class N2oAbstractChartComponent<T extends N2oAbstractChart> extends N2oComponent {
    private List<T> items;
}
