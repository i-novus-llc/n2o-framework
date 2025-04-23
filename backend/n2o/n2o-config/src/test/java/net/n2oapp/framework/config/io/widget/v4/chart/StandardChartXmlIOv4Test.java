package net.n2oapp.framework.config.io.widget.v4.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.AreaChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.ChartWidgetIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи стандартной диаграммы
 */
class StandardChartXmlIOv4Test {
    
    @Test
    void testStandardChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new AreaChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testStandardChartIOv4.widget.xml");
    }
}
