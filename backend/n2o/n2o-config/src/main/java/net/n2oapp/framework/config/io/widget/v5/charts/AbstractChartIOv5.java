package net.n2oapp.framework.config.io.widget.v5.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись абстрактного компонента диаграммы версии 5.0
 */
@Component
public abstract class AbstractChartIOv5<T extends N2oAbstractChart> implements WidgetIOv5, NamespaceIO<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
    }
}
