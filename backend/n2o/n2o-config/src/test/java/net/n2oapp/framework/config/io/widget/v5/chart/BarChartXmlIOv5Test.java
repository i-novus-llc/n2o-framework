package net.n2oapp.framework.config.io.widget.v5.chart;

import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.v5.charts.BarChartIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи гистограммы
 */
public class BarChartXmlIOv5Test {
    @Test
    public void testBarChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new BarChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testBarChartIOv5.widget.xml");
    }
}
