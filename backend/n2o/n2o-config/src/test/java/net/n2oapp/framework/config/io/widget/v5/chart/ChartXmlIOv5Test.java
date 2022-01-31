package net.n2oapp.framework.config.io.widget.v5.chart;

import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи виджета диаграммы
 */
public class ChartXmlIOv5Test {
    @Test
    public void testChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testChartIOv5.widget.xml");
    }
}
