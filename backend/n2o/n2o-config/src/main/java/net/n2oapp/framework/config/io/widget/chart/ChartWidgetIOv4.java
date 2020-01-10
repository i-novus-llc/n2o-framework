package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChartWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.WidgetElementIOv4;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись виджета-диаграммы
 */
@Component
public class ChartWidgetIOv4 extends WidgetElementIOv4<N2oChartWidget> {

    @Override
    public void io(Element e, N2oChartWidget c, IOProcessor p) {
        super.io(e, c, p);
        p.anyChild(e, null, c::getChart, c::setChart, p.anyOf(N2oChart.class), WidgetIOv4.NAMESPACE);
    }

    @Override
    public Class<N2oChartWidget> getElementClass() {
        return N2oChartWidget.class;
    }

    @Override
    public String getElementName() {
        return "chart";
    }
}
