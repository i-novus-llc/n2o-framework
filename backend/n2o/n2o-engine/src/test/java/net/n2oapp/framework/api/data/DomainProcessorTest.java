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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тесты для {@link DomainProcessor}
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
        DomainProcessor proc = new DomainProcessor();
        String nullDomain = null;
        Object val = proc.deserialize(null, nullDomain);
        assert val == null;
        val = proc.deserialize("", nullDomain);
        assert val == null;
        val = proc.deserialize(" ", nullDomain);
        assert val != null;
        val = proc.deserialize(null, "string");
        assert val == null;
        val = proc.deserialize(null, "integer");
        assert val == null;
        val = proc.deserialize("", "integer");
        assert val == null;
        val = proc.deserialize(null, "numeric");
        assert val == null;
        val = proc.deserialize("", "numeric");
        assert val == null;
        val = proc.deserialize(null, "long");
        assert val == null;
        val = proc.deserialize(null, "short");
        assert val == null;
        val = proc.deserialize(null, "byte");
        assert val == null;
    }

    @Test
    public void testDynamicValue() throws Exception {
        DomainProcessor proc = new DomainProcessor();
        assert "{id}".equals(proc.deserialize("{id}", "integer"));
        assert "`1 == 1`".equals(proc.deserialize("`1 == 1`", "boolean"));
        assert proc.deserialize("{\"id\" : 1}", "object") instanceof Map;
    }

    @Test
    public void testAutoCast() throws Exception {
        DomainProcessor proc = new DomainProcessor();
        assert proc.deserialize("abc123") instanceof String;
        assert proc.deserialize(100) instanceof Integer;
        assert proc.deserialize(100L) instanceof Long;
        assert proc.deserialize(true) instanceof Boolean;
        assert proc.deserialize("true") instanceof Boolean;
        assert proc.deserialize("123") instanceof Integer;
        assert proc.deserialize("1234567") instanceof String;//more than 6 digets
    }

    @Test
    public void testSimpleTyping() {
        DomainProcessor proc = DomainProcessor.getInstance();
        assert proc.deserialize("true", "Boolean") instanceof Boolean;
        assert proc.deserialize("1", "Byte") instanceof Byte;
        assert proc.deserialize("32", "Short") instanceof Short;
        assert proc.deserialize("123", "Integer") instanceof Integer;
        assert proc.deserialize("123", "Long") instanceof Long;
        assert proc.deserialize("123", "String") instanceof String;
        assert proc.deserialize("01.02.2014 18:15:00", "Date") instanceof Date;
        assert proc.deserialize("01.02.2014 18:15:00", "LocalDate") instanceof LocalDate;
        assert proc.deserialize("01.02.2014 18:15:00", "LocalDateTime") instanceof LocalDateTime;
        assert proc.deserialize("125.888", "Numeric") instanceof BigDecimal;
        assert proc.deserialize("11444,878", "Numeric") instanceof BigDecimal;
        Object dataSet = proc.deserialize("{\"id\":1, \"name\":\"Олег\", \"gender.name\":\"Мужской\", \"age\":\"24.5\", \"real_age\":\"29,8\"}", "Object");
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
        DomainProcessor proc = new DomainProcessor();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("01.02.2014 11:11:00");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse("02.02.2014 11:11:00");
        List<Date> list = new ArrayList<>(Arrays.asList(date1, date2));

        //уже пришли даты
        list = (List<Date>) proc.deserialize(list, "date[]");
        checkDates(date1, date2, list);

        //пришли строки
        list = (List<Date>) proc.deserialize(Arrays.asList("01.02.2014 11:11:00", "02.02.2014 11:11:00"), "date[]");
        checkDates(date1, date2, list);

        //пришли даты и без домена
        list = (List<Date>) proc.deserialize(list);
        checkDates(date1, date2, list);

        //примитивный объект, домен - массив
        assert ((List) proc.deserialize("1", "integer[]")).get(0).equals(1);
    }

    @Test
    public void testIntervals() throws Exception {
        DomainProcessor proc = new DomainProcessor();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.02.2014 11:11");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("02.02.2014 11:11");

        //уже пришли даты
        Map<String, Date> map = new HashMap<>();
        map.put("begin", date1);
        map.put("end", date2);
        checkDates(date1, date2, (Interval) proc.deserialize(map, "interval{date}"));

        //пришли строки
        Map<String, String> map2 = new HashMap<>();
        map2.put("begin", "01.02.2014 11:11:00");
        map2.put("end", "02.02.2014 11:11:00");
        checkDates(date1, date2, (Interval) proc.deserialize(map2, "interval{date}"));


    }

    @Test
    public void testDateDeserialize() {
        DomainProcessor proc = new DomainProcessor();
        assertThat(proc.deserialize("01.12.2019 23:50:40", "date"), instanceOf(Date.class));
        assertThat(proc.deserialize("01.12.2019 23:50:40", "localdatetime"), instanceOf(LocalDateTime.class));
        assertThat(proc.deserialize("01.12.2019 23:50:40", "localdate"), instanceOf(LocalDate.class));
    }

    @Test
    public void serialize() throws ParseException {
        DomainProcessor proc = new DomainProcessor();
        assert "test".equals(proc.serialize("test"));
        assert "true".equals(proc.serialize(true));
        assert "123".equals(proc.serialize(123));
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2019 11:11");
        assertThat(proc.serialize(date), is("01.01.2019 11:11:00"));
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
