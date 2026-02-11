package net.n2oapp.framework.ui.controller.export;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

class FormatUtilTest {

    @Test
    void testMaskPassword() {
        assertThat(FormatUtil.maskPassword("password"), is("********"));
        assertThat(FormatUtil.maskPassword("abc"), is("***"));
        assertThat(FormatUtil.maskPassword("a"), is("*"));
        assertThat(FormatUtil.maskPassword(""), is(""));
    }

    @Test
    void testFormatPhone() {
        assertThat(FormatUtil.formatPhone("9161234567"), is("+7 (916) 123-45-67"));
        assertThat(FormatUtil.formatPhone("79161234567"), is("+7 (916) 123-45-67"));
        assertThat(FormatUtil.formatPhone("89161234567"), is("+7 (916) 123-45-67"));
        assertThat(FormatUtil.formatPhone("+7 (916) 123-45-67"), is("+7 (916) 123-45-67"));
        assertThat(FormatUtil.formatPhone("916-123-45-67"), is("+7 (916) 123-45-67"));
        assertThat(FormatUtil.formatPhone("123"), is("123"));
        assertThat(FormatUtil.formatPhone("123456789"), is("123456789"));
    }

    @Test
    void testFormatSnils() {
        assertThat(FormatUtil.formatSnils("12345678901"), is("123-456-789 01"));
        assertThat(FormatUtil.formatSnils("123-456-789 01"), is("123-456-789 01"));
        assertThat(FormatUtil.formatSnils("123 456 789 01"), is("123-456-789 01"));
        assertThat(FormatUtil.formatSnils("1234567890"), is("1234567890"));
        assertThat(FormatUtil.formatSnils("123"), is("123"));
    }

    @Test
    void testFormatNumberWithNull() {
        assertThat(FormatUtil.formatNumber(null, "0,0"), is(nullValue()));
    }

    @Test
    void testFormatNumberWithMask() {
        assertThat(FormatUtil.formatNumber("1234567890", "+7 (000) 000-00-00"), is("+7 (123) 456-78-90"));
        assertThat(FormatUtil.formatNumber(1234567890L, "+7 (000) 000-00-00"), is("+7 (123) 456-78-90"));
    }

    @Test
    void testFormatNumberDecimal() {
        assertThat(FormatUtil.formatNumber(1234.56, "0 0.00"), is("1234,56"));
        assertThat(FormatUtil.formatNumber(1234.56, "00.00"), is("1234,56"));
        assertThat(FormatUtil.formatNumber(1234.56, "0,0.00"), is("1 234,56"));
        assertThat(FormatUtil.formatNumber(1234, "0,0"), is("1 234"));
        assertThat(FormatUtil.formatNumber("1234.56", "0,0.00"), is("1 234,56"));
    }

    @Test
    void testFormatNumberInvalidString() {
        assertThat(FormatUtil.formatNumber("not a number", "0,0"), is("not a number"));
    }

    @Test
    void testFormatDateWithNull() {
        assertThat(FormatUtil.formatDate(null, "DD.MM.YYYY"), is(nullValue()));
    }

    @Test
    void testFormatDateWithLocalDate() {
        LocalDate date = LocalDate.of(2024, 3, 15);
        assertThat(FormatUtil.formatDate(date, "DD.MM.YYYY"), is("15.03.2024"));
        assertThat(FormatUtil.formatDate(date, "YYYY-MM-DD"), is("2024-03-15"));
    }

    @Test
    void testFormatDateWithLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 14, 30, 45);
        assertThat(FormatUtil.formatDate(dateTime, "DD.MM.YYYY HH:mm:ss"), is("15.03.2024 14:30:45"));
        assertThat(FormatUtil.formatDate(dateTime, "DD.MM.YYYY"), is("15.03.2024"));
        assertThat(FormatUtil.formatDate(dateTime, "HH:mm"), is("14:30"));
    }

    @Test
    void testFormatDateWithZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2024, 3, 15, 14, 30, 45, 0, ZoneId.of("UTC"));
        assertThat(FormatUtil.formatDate(zonedDateTime, "DD.MM.YYYY HH:mm:ss"), is("15.03.2024 14:30:45"));
    }

    @Test
    void testFormatDateWithJavaUtilDate() {
        LocalDateTime ldt = LocalDateTime.of(2024, 3, 15, 14, 30, 45);
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        String result = FormatUtil.formatDate(date, "DD.MM.YYYY");
        assertThat(result, is("15.03.2024"));
    }

    @Test
    void testFormatDateWithString() {
        assertThat(FormatUtil.formatDate("2024-03-15", "DD.MM.YYYY"), is("15.03.2024"));
        assertThat(FormatUtil.formatDate("2024-03-15T14:30:45", "DD.MM.YYYY HH:mm:ss"), is("15.03.2024 14:30:45"));
    }

    @Test
    void testFormatDateWithInvalidString() {
        assertThat(FormatUtil.formatDate("invalid date", "DD.MM.YYYY"), is("invalid date"));
    }
}