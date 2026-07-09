package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.charts.AreaChartIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи диаграммы-области
 */
class AreaChartXmlIOv5Test {
    
    @Test
    void testAreaChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new AreaChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testAreaChartIOv5.widget.xml");
    }
}
