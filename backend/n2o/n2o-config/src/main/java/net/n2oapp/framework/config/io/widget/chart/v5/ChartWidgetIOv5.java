package net.n2oapp.framework.config.io.widget.chart.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.WidgetElementIOv4;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись виджета-диаграммы версии 5.0
 */
@Component
public class ChartWidgetIOv5 extends WidgetElementIOv4<N2oChart> {

    @Override
    public void io(Element e, N2oChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeInteger(e, "width", c::getWidth, c::setWidth);
        p.attributeInteger(e, "height", c::getHeight, c::setHeight);
        p.anyChild(e, null, c::getComponent, c::setComponent, p.anyOf(N2oAbstractChart.class), WidgetIOv4.NAMESPACE);
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
