package net.n2oapp.framework.config.io.widget.v4.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.BarChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.ChartWidgetIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи гистограммы
 */
class BarChartXmlIOv4Test {
    
    @Test
    void testBarChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new BarChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testBarChartIOv4.widget.xml");
    }
}
