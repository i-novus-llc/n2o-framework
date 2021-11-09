package net.n2oapp.framework.config.io.widget.v4;


import net.n2oapp.framework.config.io.action.OpenPageElementIOV1;
import net.n2oapp.framework.config.io.action.ShowModalElementIOV1;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения и записи виджета календарь
 */
public class CalendarWidgetXmlIOv4Test {
    @Test
    public void testCalendarWidgetXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CalendarWidgetIOv4(), new ShowModalElementIOV1(), new OpenPageElementIOV1());

        assert tester.check("net/n2oapp/framework/config/io/widget/calendar/testCalendarWidgetIOv4.widget.xml");
    }
}
