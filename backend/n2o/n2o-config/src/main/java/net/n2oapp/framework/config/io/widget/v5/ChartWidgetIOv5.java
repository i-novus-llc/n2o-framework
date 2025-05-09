package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v4.charts.ChartIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись виджета-диаграммы версии 5.0
 */
@Component
public class ChartWidgetIOv5 extends WidgetElementIOv5<N2oChart> {

    @Override
    public void io(Element e, N2oChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attribute(e, "height", c::getHeight, c::setHeight);
        p.anyChild(e, null, c::getComponent, c::setComponent, p.anyOf(N2oAbstractChart.class).ignore("datasource"), ChartIOv4.NAMESPACE);
        p.merge(c,getElementName());
    }

    @Override
    public Class<N2oChart> getElementClass() {
        return N2oChart.class;
    }

    @Override
    public String getElementName() {
        return "chart";
    }
}
