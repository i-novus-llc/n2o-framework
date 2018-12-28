package net.n2oapp.framework.api.metadata.control.func;

import net.n2oapp.properties.StaticProperties;

import java.util.Calendar;

/**
 * User: operhod
 * Date: 07.07.14
 * Time: 16:26
 */
public class N2oDateInfo implements DateInfo {

    private static String n2oDateFormat = StaticProperties.getProperty("n2o.format.date");

    @Override
    public String getDateFormat() {
        return n2oDateFormat;
    }

    @Override
    public Calendar getTodayCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
