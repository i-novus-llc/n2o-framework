package net.n2oapp.framework.api.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import org.junit.jupiter.api.Test;

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
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для {@link DomainProcessor}
 */
class DomainProcessorTest {

    @Test
    void testNullEmpty() {
        DomainProcessor proc = new DomainProcessor();
        String nullDomain = null;
        Object val = proc.deserialize(null, nullDomain);
        assertNull(val);
        val = proc.deserialize("", nullDomain);
        assertNull(val);
        val = proc.deserialize(" ", nullDomain);
        assertNotNull(val);
        val = proc.deserialize(null, "string");
        assertNull(val);
        val = proc.deserialize(null, "integer");
        assertNull(val);
        val = proc.deserialize("", "integer");
        assertNull(val);
        val = proc.deserialize(null, "numeric");
        assertNull(val);
        val = proc.deserialize("", "numeric");
        assertNull(val);
        val = proc.deserialize(null, "long");
        assertNull(val);
        val = proc.deserialize(null, "short");
        assertNull(val);
        val = proc.deserialize(null, "byte");
        assertNull(val);
    }

    @Test
    void testDynamicValue() {
        DomainProcessor proc = new DomainProcessor();
        assertEquals("{id}", proc.deserialize("{id}", "integer"));
        assertEquals("`1 == 1`", proc.deserialize("`1 == 1`", "boolean"));
        assertTrue(proc.deserialize("{{\"id\" : 1}}", "object") instanceof Map);
    }

    @Test
    void testAutoCast() {
        DomainProcessor proc = new DomainProcessor();
        assertTrue(proc.deserialize("abc123") instanceof String);
        assertTrue(proc.deserialize(100) instanceof Integer);
        assertTrue(proc.deserialize(100L) instanceof Long);
        assertTrue(proc.deserialize(true) instanceof Boolean);
        assertTrue(proc.deserialize("true") instanceof Boolean);
        assertTrue(proc.deserialize("123") instanceof Integer);
        assertTrue(proc.deserialize("1234567") instanceof String);//more than 6 digets
    }

    @Test
    void testEscapeString() {
        DomainProcessor proc = new DomainProcessor();
        assertThat(proc.deserialize("'true'"), is("true"));
        assertThat(proc.deserialize("''true''"), is("'true'"));
        assertThat(proc.deserialize("'''true'''"), is("''true''"));
        assertThat(proc.deserialize("'123'"), is("123"));
        assertThat(proc.deserialize("'2014-02-01T18:15:00'"), is("2014-02-01T18:15:00"));
        assertThat(proc.deserialize("some'''string"), is("some'''string"));
    }

    @Test
    void testSimpleTyping() {
        DomainProcessor proc = new DomainProcessor();
        assertTrue(proc.deserialize("true", "Boolean") instanceof Boolean);
        assertThat(proc.deserialize("1", "Byte"), instanceOf(Byte.class));
        assertThat(proc.deserialize("32", "Short"), instanceOf(Short.class));
        assertThat(proc.deserialize("123", "Integer"), instanceOf(Integer.class));
        assertThat(proc.deserialize("123", "Long"), instanceOf(Long.class));
        assertThat(proc.deserialize("123", "String"), instanceOf(String.class));
        assertThat(proc.deserialize("2014-02-01T18:15:00", "Date"), instanceOf(Date.class));
        assertThat(proc.deserialize("2014-02-01", "LocalDate"), instanceOf(LocalDate.class));
        assertThat(proc.deserialize("2014-02-01T18:15:00", "LocalDateTime"), instanceOf(LocalDateTime.class));
        assertThat(proc.deserialize("2019-12-15T23:50:40Z[Europe/Moscow]", "zoneddatetime"), instanceOf(ZonedDateTime.class));
        assertThat(proc.deserialize("2019-12-15T23:50:40-03:00", "offsetdatetime"), instanceOf(OffsetDateTime.class));
        assertThat(proc.deserialize("125.888", "Numeric"), instanceOf(BigDecimal.class));
        assertThat(proc.deserialize("11444,878", "Numeric"), instanceOf(BigDecimal.class));
        Object dataSet = proc.deserialize("{{\"id\":1, \"name\":\"Олег\", \"gender.name\":\"Мужской\", \"age\":\"24.5\", \"real_age\":\"29,8\"}}", "Object");
        assertThat(dataSet, instanceOf(DataSet.class));
        assertEquals(1, ((DataSet) dataSet).get("id"));
        assertEquals("Олег", ((DataSet) dataSet).get("name"));
        assertNotNull(((DataSet) dataSet).get("gender"));
        assertEquals("Мужской", ((DataSet) dataSet).get("gender.name"));
        assertEquals("24.5", ((DataSet) dataSet).get("age"));
        assertEquals("29,8", ((DataSet) dataSet).get("real_age"));
    }

    @Test
    void testArrays() {
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
    void testIntervals() throws Exception {
        DomainProcessor proc = new DomainProcessor();
        Date date1 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.02.2014 11:11");
        Date date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("02.02.2014 11:11");
        //мапа дат
        Map<String, Date> mapDate = new HashMap<>();
        mapDate.put("begin", date1);
        mapDate.put("end", date2);
        checkDates(date1, date2, (Interval<?>) proc.deserialize(mapDate, "interval{date}"));

        //мапа строковых дат
        Map<String, String> mapString = new HashMap<>();
        mapString.put("begin", "2014-02-01T11:11:00+04");
        mapString.put("end", "2014-02-02T11:11:00+04");
        checkDates(date1, date2, (Interval<?>) proc.deserialize(mapString, "interval{date}"));

        //мапа числел
        Map<String, Integer> mapInteger = new HashMap<>();
        mapInteger.put("begin", 1);
        mapInteger.put("end", 2);
        Object value = proc.deserialize(mapInteger, "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval<?>) value).getBegin(), is(1));
        assertThat(((Interval<?>) value).getEnd(), is(2));

        //мапа строковых чисел
        mapString = new HashMap<>();
        mapString.put("begin", "1");
        mapString.put("end", "2");
        value = proc.deserialize(mapInteger, "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval<?>) value).getBegin(), is(1));
        assertThat(((Interval<?>) value).getEnd(), is(2));

        //список чисел
        value = proc.deserialize(Arrays.asList(1, 2), "interval{integer}");
        assertThat(value, instanceOf(Interval.class));
        assertThat(((Interval<?>) value).getBegin(), is(1));
        assertThat(((Interval<?>) value).getEnd(), is(2));
    }

    @Test
    void testDateDeserialize() {
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
    void serialize() throws ParseException {
        DomainProcessor proc = new DomainProcessor();
        assertEquals("test", proc.serialize("test"));
        assertEquals("true", proc.serialize(true));
        assertEquals("123", proc.serialize(123));
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.01.2019 11:11");
        assertThat(proc.serialize(date), is("2019-01-01T11:11:00"));
    }

    private void checkDates(Date date1, Date date2, Interval<?> interval) {
        assertEquals(2, interval.size());
        assertEquals(date1, interval.getBegin());
        assertEquals(date2, interval.getEnd());
    }
}
