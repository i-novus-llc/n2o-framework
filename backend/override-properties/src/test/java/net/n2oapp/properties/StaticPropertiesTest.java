package net.n2oapp.properties;

import net.n2oapp.properties.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

import static net.n2oapp.properties.test.TestUtil.assertOnException;
import static net.n2oapp.properties.test.TestUtil.assertOnSuccess;

/**
 * @author operehod
 * @since 05.06.2015
 */
public class StaticPropertiesTest {


    private final Properties properties = new Properties();

    @Test
    void runTest() {
        //тесты не атомарные. В первом тесте происходит инициализация
        testInitialization();
        testGet();
        testList();
        testContains();
    }

    private void testContains() {
        properties.setProperty("test.property.contains1", "1");
        properties.setProperty("test.property.contains2", "2");
        assert StaticProperties.containsProperty("test.property.contains1");
        assert StaticProperties.containsProperty("test.property.contains2");
        assert !StaticProperties.containsProperty("test.property.contains3");
    }


    public void testInitialization() {
        TestUtil.Closure getProperty = () -> StaticProperties.get("test.property");
        TestUtil.Closure initProperties = () -> new StaticProperties().setProperties(properties);
        //успешно инициализируемся properties
        assertOnSuccess(initProperties);
        //делаем get, ошибки больше нет
        assertOnSuccess(getProperty);
        //инициализируемся properties еще раз, получаем ошибку
        assertOnException(initProperties, IllegalStateException.class);
    }


    public void testGet() {
        //init
        properties.setProperty("test.property.string", "someString");
        properties.setProperty("test.property.boolean", "true");
        properties.setProperty("test.property.enabled", "true");
        properties.setProperty("test.property.int", "1");
        properties.setProperty("test.property.double", "1.01");
        properties.setProperty("test.property.enum1", "tESt1");
        properties.setProperty("test.property.enum2", "t.e_s-t2");
        properties.setProperty("test.property.enum3", "t_e.s-t3");

        //string
        assert StaticProperties.get("test.property.string").equals("someString");
        assert StaticProperties.get("test.property.string2") == null;
        //boolean
        assert StaticProperties.getBoolean("test.property.boolean");
        assert !StaticProperties.getBoolean("test.property.string");
        //enabled
        assert StaticProperties.isEnabled("test.property.enabled");
        assertOnException(() -> StaticProperties.isEnabled("test.property.boolean"), IllegalArgumentException.class);
        //int
        assert StaticProperties.getInt("test.property.int") == 1;
        assertOnException(() -> StaticProperties.getInt("test.property.string"), NumberFormatException.class);
        //double
        assert StaticProperties.getDouble("test.property.double") == 1.01;
        assert StaticProperties.getDouble("test.property.int") == 1;
        assertOnException(() -> StaticProperties.getDouble("test.property.string"), NumberFormatException.class);
        //integer
        assert StaticProperties.getInteger("test.property.int") == 1;
        assert StaticProperties.getInteger("test.property.integer") == null;
        //enum
        assert StaticProperties.getEnum("test.property.enum1", TestEnum.class) == TestEnum.test1;
        assert StaticProperties.getEnum("test.property.enum2", TestEnum.class) == TestEnum.Test_2;
        assert StaticProperties.getEnum("test.property.enum3", TestEnum.class) == TestEnum.TEST__3;
        assert StaticProperties.getEnum("test.property.enum4", TestEnum.class) == null;
    }


    public void testList() {
        //init
        properties.setProperty("test.property.strings", "oleg; egor ; ; igor");
        properties.setProperty("test.property.numbers", "1, 2,    3, 7, 99");

        assert StaticProperties.getList("test.property.strings", ";").equals(Arrays.asList("oleg", "egor", "", "igor"));
        assert StaticProperties.getList("test.property.numbers", ",", Integer::parseInt).equals(Arrays.asList(1, 2, 3, 7, 99));

    }


    private enum TestEnum {
        test1, Test_2, TEST__3
    }


}
