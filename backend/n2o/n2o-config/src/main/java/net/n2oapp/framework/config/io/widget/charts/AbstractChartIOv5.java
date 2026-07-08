package net.n2oapp.framework.config.io.widget.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись абстрактного компонента диаграммы
 */
@Component
public abstract class AbstractChartIOv5<T extends N2oAbstractChart> implements ChartIOv5, NamespaceIO<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
    }
}
