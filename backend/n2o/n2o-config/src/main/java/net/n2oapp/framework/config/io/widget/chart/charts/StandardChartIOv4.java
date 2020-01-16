package net.n2oapp.framework.config.io.widget.chart.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLegendIconType;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись стандартного компонента диаграммы
 */
@Component
public abstract class StandardChartIOv4<T extends N2oStandardChart>  extends AbstractChartIOv4<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "x-data-key", c::getXAxisDataKey, c::setXAxisDataKey);
        p.attributeEnum(e, "x-orientation", c::getXAxisOrientation, c::setXAxisOrientation, N2oStandardChart.XAxisOrientationType.class);
        p.attributeBoolean(e, "x-label", c::getXLabel, c::setXLabel);
        p.attribute(e, "y-data-key", c::getYAxisDataKey, c::setYAxisDataKey);
        p.attributeEnum(e, "y-orientation", c::getYAxisOrientation, c::setYAxisOrientation, N2oStandardChart.YAxisOrientationType.class);
        p.attributeBoolean(e, "y-label", c::getYLabel, c::setYLabel);
        p.attribute(e, "grid-stroke-dasharray", c::getGridStrokeDashArray, c::setGridStrokeDashArray);
        p.attributeBoolean(e, "grid-horizontal", c::getGridHorizontal, c::setGridHorizontal);
        p.attributeBoolean(e, "grid-vertical", c::getGridVertical, c::setGridVertical);
        p.attribute(e, "tooltip-separator", c::getTooltipSeparator, c::setTooltipSeparator);
        p.attributeEnum(e, "legend-icon-type", c::getLegendIconType, c::setLegendIconType, ChartLegendIconType.class);
    }
}
