package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.charts.BarChartIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи гистограммы
 */
class BarChartXmlIOv5Test {
    
    @Test
    void testBarChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new BarChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testBarChartIOv5.widget.xml");
    }
}
