package net.n2oapp.framework.config.io.widget.v4.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.widget.v4.WidgetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись абстрактного компонента диаграммы
 */
@Component
public abstract class AbstractChartIOv4<T extends N2oAbstractChart> implements WidgetIOv4, NamespaceIO<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
    }
}
