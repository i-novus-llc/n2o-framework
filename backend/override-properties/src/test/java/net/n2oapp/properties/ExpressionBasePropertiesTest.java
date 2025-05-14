package net.n2oapp.properties;

import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author iryabov
 * @since 26.02.2016
 */
class ExpressionBasePropertiesTest {

    @Test
    void testOverride() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties(PropertiesReader
                .getPropertiesFromClasspath("web/env.properties", "web/build.properties", "web/default.properties"));
        assertEquals("env", properties.get("test.level"));
        assertEquals("env", properties.getProperty("test.level"));
    }

    @Test
    void testWithoutExpr() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "123");
        assertEquals("123", properties.get("test"));
        assertEquals("123", properties.getProperty("test"));
    }

    @Test
    void testWithExpr() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{123}");
        assertEquals("123", properties.get("test"));
        assertEquals("123", properties.getProperty("test"));

        properties.setProperty("test2", "te#{123}st");
        assertEquals("te123st", properties.get("test2"));
        assertEquals("te123st", properties.getProperty("test2"));

        properties.setProperty("test3", "te#{123}");
        assertEquals("te123", properties.get("test3"));
        assertEquals("te123", properties.getProperty("test3"));

        properties.setProperty("test4", "#{123}st");
        assertEquals("123st", properties.get("test4"));
        assertEquals("123st", properties.getProperty("test4"));
    }

    @Test
    void testExprError() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{123");
        assertThrows(Exception.class, () -> properties.get("test"));

        properties.setProperty("test", "#{{123}");
        assertThrows(Exception.class, () -> properties.get("test"));

        properties.setProperty("test", "#{}");
        assertThrows(Exception.class, () -> properties.get("test"));
    }

    @Test
    void testExprNull() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{null}");
        assertEquals("", properties.get("test"));
    }

    @Test
    void testSystemVariables() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("system", "#{systemProperties['user.name']}");
        assertEquals(properties.get("system"), System.getProperty("user.name"));
    }
}
