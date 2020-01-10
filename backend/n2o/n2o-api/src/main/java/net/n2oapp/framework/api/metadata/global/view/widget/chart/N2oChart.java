package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Абстрактная модель диаграммы
 */
@Getter
@Setter
public abstract class N2oChart extends N2oComponent {
    private Integer width;
    private Integer height;
}
