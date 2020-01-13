package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель круговой диаграммы
 */
@Getter
@Setter
public class N2oPieChart extends N2oAbstractChart {
    private Integer cx;
    private Integer cy;
    private Integer innerRadius;
    private Integer outerRadius;
    private Integer startAngle;
    private Integer endAngle;
    private String nameKey;
    private String dataKey;
    private String color;
}
