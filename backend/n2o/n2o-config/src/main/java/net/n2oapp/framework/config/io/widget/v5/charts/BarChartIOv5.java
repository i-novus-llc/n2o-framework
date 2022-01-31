package net.n2oapp.framework.config.io.widget.v5.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oBarChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oBarChartItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента гистограммы версии 5.0
 */
@Component
public class BarChartIOv5 extends StandardChartIOv5<N2oBarChart> {

    @Override
    public void io(Element e, N2oBarChart c, IOProcessor p) {
        super.io(e, c, p);
        p.children(e, null, "bar", c::getItems, c::setItems, N2oBarChartItem.class, this::barChartIOv4);
    }

    private void barChartIOv4(Element e, N2oBarChartItem i, IOProcessor p) {
        item(e, i, p);
    }

    @Override
    public Class<N2oBarChart> getElementClass() {
        return N2oBarChart.class;
    }

    @Override
    public String getElementName() {
        return "bars";
    }
}
