package net.n2oapp.framework.api.data;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * User: iryabov
 * Date: 10.04.2014
 * Time: 18:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/test-common-context.xml")
public class DomainProcessorTest {

    @Before
    public void setUp() throws Exception {
        Properties properties = (Properties) StaticSpringContext.getBean("n2oProperties");
        properties.setProperty("n2o.format.date", "dd.MM.yyyy HH:mm:ss");
    }


    @Test
    public void testNullEmpty() throws Exception {
        DomainProcessor proc = DomainProcessor.getInstance();
        Object val = proc.doDomainConversion(null, null);
        assert val == null;
        val = proc.doDomainConversion(null, "");
        assert val == null;
        val = proc.doDomainConversion(null, " ");
        assert val != null;
        val = proc.doDomainConversion("string", null);
        assert val == null;
        val = proc.doDomainConversion("integer", null);
        assert val == null;
        val = proc.doDomainConversion("integer", "");
        assert val == null;
        val = proc.doDomainConversion("numeric", null);
        assert val == null;
        val = proc.doDomainConversion("numeric", "");
        assert val == null;
        val = proc.doDomainConversion("long", null);
        assert val == null;
        val = proc.doDomainConversion("short", null);
        assert val == null;
        val = proc.doDomainConversion("byte", null);
        assert val == null;
    }

    @Test
    public void testDynamicValue() throws Exception {
        DomainProcessor proc = DomainProcessor.getInstance();
        assert "{id}".equals(proc.doDomainConversion("integer", "{id}"));
        assert "today()".equals(proc.doDomainConversion("date", "today()"));
        assert "`1 == 1`".equals(proc.doDomainConversion("boolean", "`1 == 1`"));
        assert proc.doDomainConversion("object", "{\"id\" : 1}") instanceof Map;
    }

    @Test
    public void testAutoCast() throws Exception {

        DomainProcessor proc = DomainProcessor.getInstance();
        Object val = proc.doDomainConversion(null, "abc123");
        assert val instanceof String;
        val = proc.doDomainConversion(null, "true");
        assert val instanceof Boolean;
        val = proc.doDomainConversion(null, "123");
        assert val instanceof Integer;
        val = proc.doDomainConversion(null, "01.02.2014 18:15:00");
        assert val instanceof Date;
        val = proc.doDomainConversion(null, "125,444");
        assert val instanceof BigDecimal;
        val = proc.doDomainConversion(null, "66666.5555");
        assert val instanceof BigDecimal;
        val = proc.doDomainConversion(null, "2147483648");
        assert val instanceof Long;
    }

    @Test
    public void testSimpleTyping() {
        DomainProcessor proc = DomainProcessor.getInstance();
        assert proc.doDomainConversion("Boolean", "true") instanceof Boolean;
        assert proc.doDomainConversion("Byte", "1") instanceof Byte;
        assert proc.doDomainConversion("Short", "32") instanceof Short;
        assert proc.doDomainConversion("Integer", "123") instanceof Integer;
        assert proc.doDomainConversion("Long", "123") instanceof Long;
        assert proc.doDomainConversion("String", "123") instanceof String;
        assert proc.doDomainConversion("Date", "01.02.2014 18:15:00") instanceof Date;
        assert proc.doDomainConversion("LocalDate", "01.02.2014 18:15:00") instanceof LocalDate;
        assert proc.doDomainConversion("LocalDateTime", "01.02.2014 18:15:00") instanceof LocalDateTime;
        assert proc.doDomainConversion("Numeric", "125.888") instanceof BigDecimal;
        assert proc.doDomainConversion("Numeric", "11444,878") instanceof BigDecimal;
        Object dataSet = proc.doDomainConversion("Object", "{\"id\":1, \"name\":\"Олег\", \"gender.name\":\"Мужской\", \"age\":\"24.5\", \"real_age\":\"29,8\"}");
        assert dataSet instanceof DataSet;
        assert ((DataSet) dataSet).get("id").equals(1);
        assert ((DataSet) dataSet).get("name").equals("Олег");
        assert ((DataSet) dataSet).get("gender") != null;
        assert ((DataSet) dataSet).get("gender.name").equals("Мужской");
        assert ((DataSet) dataSet).get("age").equals("24.5");
        assert ((DataSet) dataSet).get("real_age").equals("29,8");
    }

    @Test
    public void testArrays() throws Exception {
        DomainProcessor proc = DomainProcessor.getInstance();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("01.02.2014 11:11:00");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("02.02.2014 11:11:00");
        List<Date> list = new ArrayList<>(Arrays.asList(date1, date2));

        //уже пришли даты
        list = (List<Date>) proc.doDomainConversion("Date[]", list);
        checkDates(date1, date2, list);

        //пришли строки
        list = (List<Date>) proc.doDomainConversion("Date[]", Arrays.asList("01.02.2014 11:11:00", "02.02.2014 11:11:00"));
        checkDates(date1, date2, list);

        //пришли даты и без домена
        list = (List<Date>) proc.doDomainConversion(null, list);
        checkDates(date1, date2, list);

        //пришли строки и без домена
        list = (List<Date>) proc.doDomainConversion(null, Arrays.asList("01.02.2014 11:11:00", "02.02.2014 11:11:00"));
        checkDates(date1, date2, list);

//        тесты не работают, не предусмотрено в процессоре
//        //пришла строка json массив с доменом
//        list = (List<Date>) proc.doDomainConversion("Date[]", "[\"01.02.2014\", \"02.02.2014\"]");
//        checkDates(date1, date2, list);
//
//        //пришла строка json массив и без домена
//        list = (List<Date>) proc.doDomainConversion(null, "[\"01.02.2014\", \"02.02.2014\"]");
//        checkDates(date1, date2, list);
    }

    @Test
    public void testIntervals() throws Exception {
        DomainProcessor proc = DomainProcessor.getInstance();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.02.2014 11:11");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("02.02.2014 11:11");

        //уже пришли даты
        Map<String, Date> map = new HashMap();
        map.put("begin", date1);
        map.put("end", date2);
        checkDates(date1, date2, (Interval) proc.doDomainConversion("interval{date}", map));

        //пришли строки
        Map<String, String> map2 = new HashMap();
        map2.put("begin", "01.02.2014 11:11:00");
        map2.put("end", "02.02.2014 11:11:00");
        checkDates(date1, date2, (Interval) proc.doDomainConversion("interval{date}", map2));


    }

    private void checkDates(Date date1, Date date2, List<Date> list) {
        assert list.size() == 2;
        assert list.get(0).equals(date1);
        assert list.get(1).equals(date2);
    }

    private void checkDates(Date date1, Date date2, Interval interval) {
        assert interval.size() == 2;
        assert interval.getBegin().equals(date1);
        assert interval.getEnd().equals(date2);
    }
}
