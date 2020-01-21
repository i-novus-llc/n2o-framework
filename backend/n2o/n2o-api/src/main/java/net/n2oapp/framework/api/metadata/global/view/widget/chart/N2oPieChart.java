package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель круговой диаграммы
 */
@Getter
@Setter
public class N2oPieChart extends N2oAbstractChart {
    private Integer centerX;
    private Integer centerY;
    private Integer innerRadius;
    private Integer outerRadius;
    private Integer startAngle;
    private Integer endAngle;
    private String nameFieldId;
    private String valueFieldId;
    private String color;
    private Boolean hasLabel;
}
