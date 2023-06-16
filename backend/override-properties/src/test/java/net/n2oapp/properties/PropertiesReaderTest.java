package net.n2oapp.properties;

import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * User: operhod
 * Date: 07.04.14
 * Time: 16:53
 */
public class PropertiesReaderTest {


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
        try(PrintWriter out = new PrintWriter(file)){
            out.println("test=123");
        }
        file.deleteOnExit();
        ReloadableProperties fsProps = PropertiesReader.getReloadableFromFilesystem(file.getAbsolutePath(), 60);
        assert "123".equals(fsProps.get("test"));
    }

    private void checkContextPathAndProp1AndProp2Merge(Properties properties) {
        //get
        assert properties.get("test.one").equals("one");
        assert properties.get("test.two").equals("two");
        assert properties.get("test.three").equals("three");
        assert properties.get("test.four").equals("test");
        assert properties.get("test.five").equals("test");
        //getProperty
        assert properties.getProperty("test.one").equals("one");
        assert properties.getProperty("test.two").equals("two");
        assert properties.getProperty("test.three").equals("three");
        assert properties.getProperty("test.four").equals("test");
        assert properties.getProperty("test.five").equals("test");
        //containsKey
        assert properties.containsKey("test.one");
        assert properties.containsKey("test.two");
        assert properties.containsKey("test.three");
        assert properties.containsKey("test.four");
        assert properties.containsKey("test.five");
        assert !properties.containsKey("test.six");
        //containsValue
        assert properties.contains("one");
        assert properties.contains("two");
        assert properties.contains("three");
        assert properties.contains("test");
        assert !properties.contains("six");
        assert properties.containsValue("one");
        assert properties.containsValue("two");
        assert properties.containsValue("three");
        assert properties.containsValue("test");
        assert !properties.containsValue("six");
    }


    private void checkProp1AndProp2Merge(Properties properties) {
        assert properties.get("test.one").equals("one");
        assert properties.get("test.two").equals("two");
        assert properties.get("test.three").equals("three");
        assert properties.get("test.four").equals("four");
        assert properties.getProperty("test.one").equals("one");
        assert properties.getProperty("test.two").equals("two");
        assert properties.getProperty("test.three").equals("three");
        assert properties.getProperty("test.four").equals("four");
        assert properties.containsKey("test.one");
        assert properties.containsKey("test.two");
        assert properties.containsKey("test.three");
        assert properties.containsKey("test.four");
        assert !properties.containsKey("test.five");
        assert properties.contains("one");
        assert properties.contains("two");
        assert properties.contains("three");
        assert properties.contains("four");
        assert !properties.contains("five");
        assert properties.containsValue("one");
        assert properties.containsValue("two");
        assert properties.containsValue("three");
        assert properties.containsValue("four");
        assert !properties.containsValue("five");
    }




}
