package net.n2oapp.framework.api.util;

import net.n2oapp.framework.api.StringUtils;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

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
        assert StringUtils.isDynamicValue("tomorrow()");
        assert StringUtils.isDynamicValue("`1==1`");
    }

    @Test
    public void isLink() {
        assertThat(StringUtils.isLink("abc"), is(false));
    }
}
