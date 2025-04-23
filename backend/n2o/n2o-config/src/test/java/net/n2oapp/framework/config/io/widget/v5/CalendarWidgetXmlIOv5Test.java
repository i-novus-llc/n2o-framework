package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.action.v2.ShowModalElementIOV2;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения и записи виджета календарь
 */
class CalendarWidgetXmlIOv5Test {
    
    @Test
    void testCalendarWidgetXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardDatasourceIO(), new CalendarWidgetIOv5(), new ShowModalElementIOV2(), new OpenPageElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/widget/calendar/testCalendarWidgetIOv5.widget.xml");
    }
}
