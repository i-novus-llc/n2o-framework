package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static net.n2oapp.framework.api.StringUtils.isBoolean;
import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author iryabov
 * @since 29.07.2016
 */
class StringUtilsTest {

    @Test
    void resolvePlaceHoldersTest() {
        //map for PlaceHolders
        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("name", "john");

        //простой тест
        String text = "${name}";
        assertEquals("john", StringUtils.resolveProperties(text, map));

        //пробелы
        text = " ${name} ";
        assertEquals(" john ", StringUtils.resolveProperties(text, map));

        //доп символы
        text = "1${id}3";
        assertEquals("123", StringUtils.resolveProperties(text, map));

        //json
        text = "{\"id\":${id}, \"name\":\"${name}\"}";
        System.out.println("before: " + text);
        String res = StringUtils.resolveProperties(text, map);
        assertEquals("{\"id\":2, \"name\":\"john\"}", StringUtils.resolveProperties(text, map));
        System.out.println("after: " + res);
    }

    @Test
    void testIsDynamicValue() {
        assertFalse(StringUtils.isDynamicValue(1));
        assertFalse(StringUtils.isDynamicValue(new Date()));
        assertFalse(StringUtils.isDynamicValue("Олег"));
        assertTrue(StringUtils.isDynamicValue("{id}"));
        assertTrue(StringUtils.isDynamicValue("{$.now()}"));
        assertTrue(StringUtils.isDynamicValue("tomorrow()"));
        assertTrue(StringUtils.isDynamicValue("`1==1`"));
    }

    @Test
    void isLink() {
        assertThat(StringUtils.isLink("abc"), is(false));
    }

    @Test
    void unwrapLink() {
        assertThat(StringUtils.unwrapLink("{text}"), is("text"));
        assertThat(StringUtils.unwrapLink("text"), nullValue());
        assertThat(StringUtils.unwrapLink("`text`"), nullValue());
    }

    @Test
    void isEscapedString() {
        assertThat(StringUtils.isEscapedString("'123'"), is(true));
        assertThat(StringUtils.isEscapedString("''123''"), is(true));
        assertThat(StringUtils.isEscapedString("'false'"), is(true));
        assertThat(StringUtils.isEscapedString("false"), is(false));
    }

    @Test
    void unwrapEscapedString() {
        assertThat(StringUtils.unwrapEscapedString("'true'"), is("true"));
        assertThat(StringUtils.unwrapEscapedString("'''text'''"), is("''text''"));
        assertThat(StringUtils.unwrapEscapedString("'123'"), is("123"));
        assertThat(StringUtils.unwrapEscapedString("123"), is("123"));
    }

    @Test
    void hasLink() {
        assertThat(StringUtils.hasLink("{test}"), is(true));
        assertThat(StringUtils.hasLink("{}"), is(false));
        assertThat(StringUtils.hasLink("#{test}"), is(false));
        assertThat(StringUtils.hasLink("${test}"), is(false));
        assertThat(StringUtils.hasLink("test"), is(false));
        assertThat(StringUtils.hasLink("test1 {test2} test3"), is(true));
        assertThat(StringUtils.hasLink("test1 #{test2} test3"), is(false));
        assertThat(StringUtils.hasLink("test1 ${test2} test3"), is(false));
        assertThat(StringUtils.hasLink("#{test} - #{test2}"), is(false));
        assertThat(StringUtils.hasLink("{test} - #{test2}"), is(true));
        assertThat(StringUtils.hasLink("#{test} - {test2}"), is(true));
        assertThat(StringUtils.hasLink("{test}\n"), is(true));
        assertThat(StringUtils.hasLink("\n{test}\n"), is(true));
        assertThat(StringUtils.hasLink("\n"), is(false));
    }

    @Test
    void testMaskEquals() {
        assertTrue(StringUtils.maskMatch("*", "test"));
        assertTrue(StringUtils.maskMatch("1Aba?", "1Aba?"));
        assertFalse(StringUtils.maskMatch("1Aba", "0Aba"));

        assertTrue(StringUtils.maskMatch("1Aba?*", "1Aba?"));
        assertFalse(StringUtils.maskMatch("1Aba*", "1A000ba"));
        assertFalse(StringUtils.maskMatch("1Aba*", "0001Aba"));
        assertTrue(StringUtils.maskMatch("1Aba*", "1Aba1000"));

        assertTrue(StringUtils.maskMatch("*1Aba?", "1Aba?"));
        assertFalse(StringUtils.maskMatch("*1Aba", "1A000ba"));
        assertTrue(StringUtils.maskMatch("*1Aba", "0001Aba"));
        assertFalse(StringUtils.maskMatch("*1Aba", "1Aba1000"));

        assertTrue(StringUtils.maskMatch("1A*ba?", "1Aba?"));
        assertTrue(StringUtils.maskMatch("1A*ba", "1A000ba"));
        assertFalse(StringUtils.maskMatch("1A*ba", "0001Aba"));
        assertFalse(StringUtils.maskMatch("1A*ba", "1Aba000"));

        assertTrue(StringUtils.maskMatch("*1Aba?*", "1Aba?"));
        assertTrue(StringUtils.maskMatch("*1Aba*", "zzz1Aba"));
        assertTrue(StringUtils.maskMatch("*1Aba*", "1Abazzz"));
        assertTrue(StringUtils.maskMatch("*1Aba*", "zzz1Abazzz"));
    }

    @Test
    void testPrepareSizeAttribute() {
        assertThat(prepareSizeAttribute("100"), is("100px"));
        assertThat(prepareSizeAttribute("100px"), is("100px"));
        assertThat(prepareSizeAttribute("100em"), is("100em"));
        assertThat(prepareSizeAttribute("100.5"), is("100.5px"));
        assertThat(prepareSizeAttribute(""), is(""));
        assertThat(prepareSizeAttribute(null), nullValue());
    }

    @Test
    void testIsBoolean() {
        assertThat(isBoolean("true"), is(true));
        assertThat(isBoolean("false"), is(true));
        assertThat(isBoolean("test"), is(false));
        assertThat(isBoolean("1"), is(false));
        assertThat(isBoolean(null), is(false));
    }
}