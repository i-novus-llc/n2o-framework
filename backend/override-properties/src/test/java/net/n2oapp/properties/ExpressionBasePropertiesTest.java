package net.n2oapp.properties;

import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.jupiter.api.Test;

/**
 * @author iryabov
 * @since 26.02.2016
 */
public class ExpressionBasePropertiesTest {

    @Test
    void testOverride() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties(PropertiesReader
                .getPropertiesFromClasspath("web/env.properties", "web/build.properties", "web/default.properties"));
        assert properties.get("test.level").equals("env");
        assert properties.getProperty("test.level").equals("env");
    }

    @Test
    void testWithoutExpr() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "123");
        assert properties.get("test").equals("123");
        assert properties.getProperty("test").equals("123");
    }

    @Test
    void testWithExpr() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{123}");
        assert properties.get("test").equals("123");
        assert properties.getProperty("test").equals("123");

        properties.setProperty("test2", "te#{123}st");
        assert properties.get("test2").equals("te123st");
        assert properties.getProperty("test2").equals("te123st");

        properties.setProperty("test3", "te#{123}");
        assert properties.get("test3").equals("te123");
        assert properties.getProperty("test3").equals("te123");

        properties.setProperty("test4", "#{123}st");
        assert properties.get("test4").equals("123st");
        assert properties.getProperty("test4").equals("123st");
    }

    @Test
    void testExprError() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{123");
        try {
            properties.get("test");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        properties.setProperty("test", "#{{123}");
        try {
            properties.get("test");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        properties.setProperty("test", "#{}");
        try {
            properties.get("test");
            assert false;
        } catch (Exception e) {
            assert true;
        }

    }

    @Test
    void testExprNull() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("test", "#{null}");
        assert "".equals(properties.get("test"));
    }

    @Test
    void testSystemVariables() {
        ExpressionBasedProperties properties = new ExpressionBasedProperties();
        properties.setProperty("system", "#{systemProperties['user.name']}");
        assert properties.get("system").equals(System.getProperty("user.name"));
    }
}
