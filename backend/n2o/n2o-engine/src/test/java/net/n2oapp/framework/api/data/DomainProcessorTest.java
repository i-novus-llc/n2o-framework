package net.n2oapp.framework.api.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тесты для {@link DomainProcessor}
 */
public class DomainProcessorTest {

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
        assert proc.deserialize("{{\"id\" : 1}}", "object") instanceof Map;
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
    public void testEscapeString() {
        DomainProcessor proc = new DomainProcessor();
        assertThat(proc.deserialize("'true'"), is("true"));
        assertThat(proc.deserialize("''true''"), is("'true'"));
        assertThat(proc.deserialize("'''true'''"), is("''true''"));
        assertThat(proc.deserialize("'123'"), is("123"));
        assertThat(proc.deserialize("'2014-02-01T18:15:00'"), is("2014-02-01T18:15:00"));
        assertThat(proc.deserialize("some'''string"), is("some'''string"));
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
        assert proc.deserialize("2014-02-01T18:15:00", "Date") instanceof Date;
        assert proc.deserialize("2014-02-01", "LocalDate") instanceof LocalDate;
        assert proc.deserialize("2014-02-01T18:15:00", "LocalDateTime") instanceof LocalDateTime;
        assert proc.deserialize("2019-12-15T23:50:40Z[Europe/Moscow]", "zoneddatetime") instanceof ZonedDateTime;
        assert proc.deserialize("2019-12-15T23:50:40-03:00", "offsetdatetime") instanceof OffsetDateTime;
        assert proc.deserialize("125.888", "Numeric") instanceof BigDecimal;
        assert proc.deserialize("11444,878", "Numeric") instanceof BigDecimal;
        Object dataSet = proc.deserialize("{{\"id\":1, \"name\":\"Олег\", \"gender.name\":\"Мужской\", \"age\":\"24.5\", \"real_age\":\"29,8\"}}", "Object");
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

        //список чисел с доменом
        Object value = proc.deserialize(Arrays.asList("1", "2"), "integer[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is(1));
        assertThat(((List<?>) value).get(1), is(2));

        //список чисел без домена (автоподбор домена)
        value = proc.deserialize(Arrays.asList("1", "2"));
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is(1));
        assertThat(((List<?>) value).get(1), is(2));

        //список чисел в виде строки
        value = proc.deserialize("1,2", "integer[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is(1));
        assertThat(((List<?>) value).get(1), is(2));

        //список чисел в виде строки json
        value = proc.deserialize("[1,2]", "integer[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is(1));
        assertThat(((List<?>) value).get(1), is(2));

        //список строк в виде строки
        value = proc.deserialize("a,b", "string[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is("a"));
        assertThat(((List<?>) value).get(1), is("b"));

        //json строк
        value = proc.deserialize("[\"a\",\"b\"]", "string[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is("a"));
        assertThat(((List<?>) value).get(1), is("b"));

        //json строковых чисел
        value = proc.deserialize("[\"1\",\"2\"]", "integer[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(2));
        assertThat(((List<?>) value).get(0), is(1));
        assertThat(((List<?>) value).get(1), is(2));

        //одно число как список
        value = proc.deserialize("1", "integer[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(1));
        assertThat(((List<?>) value).get(0), is(1));

        //одна строка как список
        value = proc.deserialize("a", "string[]");
        assertThat(value, instanceOf(List.class));
        assertThat(((List<?>) value).size(), is(1));
        assertThat(((List<?>) value).get(0), is("a"));
    }

    @Test
    public void testIntervals() throws Exception {
        DomainProcessor proc = new DomainProcessor();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.02.2014 11:11");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("02.02.2014 11:11");
        //мапа дат
        Map<String, Date> mapDate = new HashMap<>();
        mapDate.put("begin", date1);
        mapDate.put("end", date2);
        checkDates(date1, date2, (Interval) proc.deserialize(mapDate, "interval{date}"));

        //мапа строковых дат
        Map<String, String> mapString = new HashMap<>();
        mapString.put("begin", "2014-02-01T11:11:00+04");
        mapString.put("end", "2014-02-02T11:11:00+04");
        checkDates(date1, date2, (Interval) proc.deserialize(mapString, "interval{date}"));

        //мапа числел
        Map<String, Integer> mapInteger = new HashMap<>();
        mapInteger.put("begin", 1);
        mapInteger.put("end", 2);
        Object value = proc.deserialize(mapInteger, "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval) value).getBegin(), is(1));
        assertThat(((Interval) value).getEnd(), is(2));

        //мапа строковых чисел
        mapString = new HashMap<>();
        mapString.put("begin", "1");
        mapString.put("end", "2");
        value = proc.deserialize(mapInteger, "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval) value).getBegin(), is(1));
        assertThat(((Interval) value).getEnd(), is(2));

        //список чисел
        value = proc.deserialize(Arrays.asList(1, 2), "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval) value).getBegin(), is(1));
        assertThat(((Interval) value).getEnd(), is(2));
    }

    @Test
    public void testDateDeserialize() {
        DomainProcessor proc = new DomainProcessor();
        assertThat(proc.deserialize("2019-12-15T23:50:40", "date"), instanceOf(Date.class));
        assertThat(proc.deserialize("2019-12-01", "localdate"), instanceOf(LocalDate.class));
        assertThat(proc.deserialize("2019-12-01T00:00:00", "localdate"), instanceOf(LocalDate.class));
        assertThat(proc.deserialize("2019-12-01T23:50:40", "localdatetime"), instanceOf(LocalDateTime.class));
        assertThat(proc.deserialize("2019-12-01T23:50:40.200", "localdatetime"), instanceOf(LocalDateTime.class));
        assertThat(proc.deserialize("2019-12-15T23:50:40+03:00", "offsetdatetime"), instanceOf(OffsetDateTime.class));
        assertThat(proc.deserialize("2019-12-15T23:50:40+03:00", "zoneddatetime"), instanceOf(ZonedDateTime.class));
        assertThat(proc.deserialize("2019-12-15T23:50:40+03:00[Europe/Moscow]", "zoneddatetime"), instanceOf(ZonedDateTime.class));
    }

    @Test
    public void serialize() throws ParseException {
        DomainProcessor proc = new DomainProcessor();
        assert "test".equals(proc.serialize("test"));
        assert "true".equals(proc.serialize(true));
        assert "123".equals(proc.serialize(123));
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2019 11:11");
        assertThat(proc.serialize(date), is("2019-01-01T11:11:00"));
    }

    private void checkDates(Date date1, Date date2, Interval interval) {
        assert interval.size() == 2;
        assert interval.getBegin().equals(date1);
        assert interval.getEnd().equals(date2);
    }
}
