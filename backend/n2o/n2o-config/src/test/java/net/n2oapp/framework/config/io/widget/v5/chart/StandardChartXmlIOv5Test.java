package net.n2oapp.framework.config.io.widget.v5.chart;

import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.v5.charts.AreaChartIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи стандартной диаграммы
 */
public class StandardChartXmlIOv5Test {
    @Test
    public void testStandardChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new AreaChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testStandardChartIOv5.widget.xml");
    }
}
