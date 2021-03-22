package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Исходная модель стандартной диаграммы
 */
@Getter
@Setter
public abstract class N2oStandardChartItem extends N2oComponent {
    private String fieldId;
    private String label;
    private String color;
    private Boolean hasLabel;
}
