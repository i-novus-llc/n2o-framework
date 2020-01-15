package net.n2oapp.framework.config.io.widget.chart.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
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
        p.attributeInteger(e, "x-tick-count", c::getXAxisTickCount, c::setXAxisTickCount);
        p.attribute(e, "y-data-key", c::getYAxisDataKey, c::setYAxisDataKey);
        p.attributeInteger(e, "y-tick-count", c::getYAxisTickCount, c::setYAxisTickCount);
        p.attributeInteger(e, "grid-x", c::getGridX, c::setGridX);
        p.attributeInteger(e, "grid-y", c::getGridY, c::setGridY);
        p.attributeInteger(e, "grid-width", c::getGridWidth, c::setGridWidth);
        p.attributeInteger(e, "grid-height", c::getGridHeight, c::setGridHeight);
        p.attribute(e, "tooltip-separator", c::getTooltipSeparator, c::setTooltipSeparator);
        p.attributeInteger(e, "legend-width", c::getLegendWidth, c::setLegendWidth);
        p.attributeInteger(e, "legend-height", c::getLegendHeight, c::setLegendHeight);
    }
}
