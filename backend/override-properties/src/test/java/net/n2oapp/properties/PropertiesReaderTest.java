package net.n2oapp.properties;

import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User: operhod
 * Date: 07.04.14
 * Time: 16:53
 */
class PropertiesReaderTest {


    @Test
    void readFromTwoProperties() {
        checkProp1AndProp2Merge(PropertiesReader.getPropertiesFromClasspath("prop2.properties", "prop1.properties"));
    }


    @Test
    void readFromThreeProperties() {
        checkContextPathAndProp1AndProp2Merge(PropertiesReader
                .getPropertiesFromClasspath("context-path.properties", "prop2.properties", "prop1.properties"));
    }

    @Test
    void readFromPropertiesWithNullProperties() {

        checkProp1AndProp2Merge(PropertiesReader
                .getPropertiesFromClasspath("not_exists.properties", "prop2.properties", "prop1.properties"));
        checkProp1AndProp2Merge(PropertiesReader
                .getPropertiesFromClasspath("prop2.properties", "not_exists.properties", "prop1.properties"));
        checkProp1AndProp2Merge(PropertiesReader
                .getPropertiesFromClasspath("prop2.properties", "prop1.properties", "not_exists.properties"));
    }

    @Test
    void testFileSystemProperties() throws Exception {
        File file = File.createTempFile("proptest", "properties");
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("test=123");
        }
        file.deleteOnExit();
        ReloadableProperties fsProps = PropertiesReader.getReloadableFromFilesystem(file.getAbsolutePath(), 60);
        assertEquals("123", fsProps.get("test"));
    }

    private void checkContextPathAndProp1AndProp2Merge(Properties properties) {
        //get
        assertEquals("one", properties.get("test.one"));
        assertEquals("two", properties.get("test.two"));
        assertEquals("three", properties.get("test.three"));
        assertEquals("test", properties.get("test.four"));
        assertEquals("test", properties.get("test.five"));
        //getProperty
        assertEquals("one", properties.getProperty("test.one"));
        assertEquals("two", properties.getProperty("test.two"));
        assertEquals("three", properties.getProperty("test.three"));
        assertEquals("test", properties.getProperty("test.four"));
        assertEquals("test", properties.getProperty("test.five"));
        //containsKey
        assertTrue(properties.containsKey("test.one"));
        assertTrue(properties.containsKey("test.two"));
        assertTrue(properties.containsKey("test.three"));
        assertTrue(properties.containsKey("test.four"));
        assertTrue(properties.containsKey("test.five"));
        assertFalse(properties.containsKey("test.six"));
        //containsValue
        assertTrue(properties.contains("one"));
        assertTrue(properties.contains("two"));
        assertTrue(properties.contains("three"));
        assertTrue(properties.contains("test"));
        assertFalse(properties.contains("six"));
        assertTrue(properties.containsValue("one"));
        assertTrue(properties.containsValue("two"));
        assertTrue(properties.containsValue("three"));
        assertTrue(properties.containsValue("test"));
        assertFalse(properties.containsValue("six"));
    }

    private void checkProp1AndProp2Merge(Properties properties) {
        assertEquals("one", properties.get("test.one"));
        assertEquals("two", properties.get("test.two"));
        assertEquals("three", properties.get("test.three"));
        assertEquals("four", properties.get("test.four"));
        assertEquals("one", properties.getProperty("test.one"));
        assertEquals("two", properties.getProperty("test.two"));
        assertEquals("three", properties.getProperty("test.three"));
        assertEquals("four", properties.getProperty("test.four"));
        assertTrue(properties.containsKey("test.one"));
        assertTrue(properties.containsKey("test.two"));
        assertTrue(properties.containsKey("test.three"));
        assertTrue(properties.containsKey("test.four"));
        assertFalse(properties.containsKey("test.five"));
        assertTrue(properties.contains("one"));
        assertTrue(properties.contains("two"));
        assertTrue(properties.contains("three"));
        assertTrue(properties.contains("four"));
        assertFalse(properties.contains("five"));
        assertTrue(properties.containsValue("one"));
        assertTrue(properties.containsValue("two"));
        assertTrue(properties.containsValue("three"));
        assertTrue(properties.containsValue("four"));
        assertFalse(properties.containsValue("five"));
    }
}