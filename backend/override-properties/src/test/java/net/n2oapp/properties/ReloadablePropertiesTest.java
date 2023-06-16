package net.n2oapp.properties;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * @author operehod
 * @since 05.06.2015
 */
public class ReloadablePropertiesTest {

    @Test
    @Disabled
    void test() throws Exception {
        ReloadableProperties properties = new ReloadableProperties(new ClassPathResource("prop1.properties"));
        properties.setCacheTime(1);
        assert properties.getResource().getFilename().equals("prop1.properties");
        assert properties.getProperty("test.one").equals("one");
        assert properties.getProperty("test.two").equals("error");
        assert properties.getProperty("test.three").equals("error");
        //меняем исходный файл
        properties.setResource(new ClassPathResource("prop2.properties"));
        assert properties.getResource().getFilename().equals("prop2.properties");
        //properties не меняются
        assert properties.getProperty("test.one").equals("one");
        assert properties.getProperty("test.two").equals("error");
        assert properties.getProperty("test.three").equals("error");
        //ждем полторый секунды
        Thread.sleep(1500);
        //properties поменялись
        assert properties.getResource().getFilename().equals("prop2.properties");
        assert properties.getProperty("test.two").equals("two");
        assert properties.getProperty("test.three").equals("three");
        assert properties.getProperty("test.four").equals("four");

        properties.updateProperty("newProperty", "newValue");
        Thread.sleep(1500);
        assert properties.getProperty("newProperty").equals("newValue");
    }

    @Test
    @Disabled
    void testFileNotExist() throws InterruptedException {
        FileSystemResource fileSystemResource = new FileSystemResource(System.getProperty("user.home") + File.separator + "test.properties");
        fileSystemResource.getFile().deleteOnExit();

        ReloadableProperties properties = new ReloadableProperties(fileSystemResource);
        properties.setCacheTime(1);
        properties.updateProperty("newProperty", "newValue");

        Thread.sleep(1500);
        assert properties.getProperty("newProperty").equals("newValue");

        properties.removeProperty("newProperty");
        Thread.sleep(1500);

        assert properties.getProperty("newProperty") == null;

    }
}
