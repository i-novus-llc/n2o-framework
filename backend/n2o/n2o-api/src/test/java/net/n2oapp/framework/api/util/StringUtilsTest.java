package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.StringUtils;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author iryabov
 * @since 29.07.2016
 */
public class StringUtilsTest {

    @Test
    public void resolvePlaceHoldersTest() {
        //map for PlaceHolders
        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("name", "john");

        //простой тест
        String text = "${name}";
        assert StringUtils.resolveProperties(text, map).equals("john");

        //пробелы
        text = " ${name} ";
        assert StringUtils.resolveProperties(text, map).equals(" john ");

        //доп символы
        text = "1${id}3";
        assert StringUtils.resolveProperties(text, map).equals("123");

        //json
        text = "{\"id\":${id}, \"name\":\"${name}\"}";
        System.out.println("before: " + text);
        String res = StringUtils.resolveProperties(text, map);
        assert StringUtils.resolveProperties(text, map).equals("{\"id\":2, \"name\":\"john\"}");
        System.out.println("after: " + res);
    }

    @Test
    public void testIsDynamicValue() throws Exception {
        assert !StringUtils.isDynamicValue(1);
        assert !StringUtils.isDynamicValue(new Date());
        assert !StringUtils.isDynamicValue("Олег");
        assert StringUtils.isDynamicValue("{id}");
        assert StringUtils.isDynamicValue("{$.now()}");
        assert StringUtils.isDynamicValue("tomorrow()");
        assert StringUtils.isDynamicValue("`1==1`");
    }

    @Test
    public void isLink() {
        assertThat(StringUtils.isLink("abc"), is(false));
    }

    @Test
    public void unwrapLink() {
        assertThat(StringUtils.unwrapLink("{text}"), is("text"));
        assertThat(StringUtils.unwrapLink("text"), nullValue());
        assertThat(StringUtils.unwrapLink("`text`"), nullValue());
    }

    @Test
    public void isEscapedString() {
        assertThat(StringUtils.isEscapedString("'123'"), is(true));
        assertThat(StringUtils.isEscapedString("''123''"), is(true));
        assertThat(StringUtils.isEscapedString("'false'"), is(true));
        assertThat(StringUtils.isEscapedString("false"), is(false));
    }

    @Test
    public void unwrapEscapedString() {
        assertThat(StringUtils.unwrapEscapedString("'true'"), is("true"));
        assertThat(StringUtils.unwrapEscapedString("'''text'''"), is("''text''"));
        assertThat(StringUtils.unwrapEscapedString("'123'"), is("123"));
        assertThat(StringUtils.unwrapEscapedString("123"), is("123"));
    }

    @Test
    public void hasLink() {
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
    }

    @Test
    public void testMaskEquals() {
        assert StringUtils.maskMatch("*", "test");
        assert StringUtils.maskMatch("1Aba?", "1Aba?");
        assert !StringUtils.maskMatch("1Aba", "0Aba");

        assert StringUtils.maskMatch("1Aba?*", "1Aba?");
        assert !StringUtils.maskMatch("1Aba*", "1A000ba");
        assert !StringUtils.maskMatch("1Aba*", "0001Aba");
        assert StringUtils.maskMatch("1Aba*", "1Aba1000");

        assert StringUtils.maskMatch("*1Aba?", "1Aba?");
        assert !StringUtils.maskMatch("*1Aba", "1A000ba");
        assert StringUtils.maskMatch("*1Aba", "0001Aba");
        assert !StringUtils.maskMatch("*1Aba", "1Aba1000");

        assert StringUtils.maskMatch("1A*ba?", "1Aba?");
        assert StringUtils.maskMatch("1A*ba", "1A000ba");
        assert !StringUtils.maskMatch("1A*ba", "0001Aba");
        assert !StringUtils.maskMatch("1A*ba", "1Aba000");

        assert StringUtils.maskMatch("*1Aba?*", "1Aba?");
        assert StringUtils.maskMatch("*1Aba*", "zzz1Aba");
        assert StringUtils.maskMatch("*1Aba*", "1Abazzz");
        assert StringUtils.maskMatch("*1Aba*", "zzz1Abazzz");
    }

    @Test
    public void simplify() {
        assert StringUtils.simplify("").equals("");
        assert StringUtils.simplify("  abc  ").equals("abc");
        assert StringUtils.simplify("\nabc\n").equals("abc");
        assert StringUtils.simplify("  \n  abc  \n  ").equals("abc");
        assert StringUtils.simplify("  \n  \n abc \n \n  ").equals("abc");
    }
}
